package sparta.m6nytooneproject.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.global.exception.product.ProductNotFoundException;
import sparta.m6nytooneproject.product.dto.*;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.Status;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.review.entity.Review;
import sparta.m6nytooneproject.review.repository.ReviewRepository;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.service.UserService;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ProductResponseDto createProduct(Long userId, ProductRequestDto request) {
        User admin = userService.getUserById(userId);
        Product product = new Product(request.getProductName(), request.getCategory(), request.getPrice(), request.getStock(), request.getStatus(), admin);
        Product savedProduct = productRepository.save(product);
        return new ProductResponseDto(savedProduct);
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable, String productName, Category category, Status status) {

        if (productName != null && category == null && status == null) {
            // 검색 키워드 상품명 조회
            return productRepository.findByProductNameContaining(productName, pageable)
                    .map(ProductResponseDto::new);
        } else if (productName == null && category != null && status == null) {
            // 카테고리 필터
            return productRepository.findByCategory(category, pageable)
                    .map(ProductResponseDto::new);
        } else if (productName == null && category == null && status != null) {
            // 상품상태 필터
            return productRepository.findByStatus(status, pageable)
                    .map(ProductResponseDto::new);
        } else if (productName != null && category != null && status == null) {
            // 검색 키워드 상품명 조회와 카테고리 필터
            return productRepository.findByProductNameAndCategory(productName, category, pageable)
                    .map(ProductResponseDto::new);
        } else if (productName != null && category == null && status != null) {
            // 검색 키워드 상품명 조회와 상품상태 필터
            return productRepository.findByProductNameAndStatus(productName, status, pageable)
                    .map(ProductResponseDto::new);
        } else if (productName == null && category != null && status != null) {
            // 카테고리 필터와 상품상태 필터
            return productRepository.findByCategoryAndStatus(category, status, pageable)
                    .map(ProductResponseDto::new);
        } else if (productName != null && category != null && status != null) {
            // 검색 키워드 상품명 조회와 카테고리 필터와 상품상태 필터
            return productRepository.findByProductNameAndCategoryAndStatus(productName, category, status, pageable)
                    .map(ProductResponseDto::new);
        }
        // 전체 조회
        return productRepository.findAll(pageable).map(ProductResponseDto::new);
    }

    public GetOneProductResponseDto getOneProduct(Long productId) {
        Product product = getProductById(productId);
        // 평균 평점
        List<Review> reviews = reviewRepository.findAllReviewByProductId(productId);
        if (reviews.isEmpty()) {
            return new GetOneProductResponseDto(product, null, 0, null, null);
        }
        List<Integer> reviewRates = reviews.stream().map(Review::getReviewRate).toList();
        int sum = reviewRates.stream().mapToInt(Integer::intValue).sum();
        String averageRate = String.format("%.1f", (double) sum / reviews.size());
        // 전체 리뷰 개수
        int reviewCount = reviews.size();
        // 별점별 개수
        Map<Integer, Integer> rating = new HashMap<>();
        rating.put(5, reviewRepository.findRateReviewByProductId(productId, 5).size());
        rating.put(4, reviewRepository.findRateReviewByProductId(productId, 4).size());
        rating.put(3, reviewRepository.findRateReviewByProductId(productId, 3).size());
        rating.put(2, reviewRepository.findRateReviewByProductId(productId, 2).size());
        rating.put(1, reviewRepository.findRateReviewByProductId(productId, 1).size());
        ObjectMapper mapper = new ObjectMapper();
        String ratingCounts = mapper.writeValueAsString(rating);
        // 최신 리뷰
        List<Review> recentReviews = reviewRepository.findTop3ByProductIdOrderByCreatedAtDesc(productId);
        return new GetOneProductResponseDto(product, averageRate, reviewCount, ratingCounts, recentReviews);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, UpdateProductRequestDto request) {
        Product product = getProductById(productId);
        product.updateProduct(request.getProductName(), request.getCategory(), request.getPrice());
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProductStock(Long productId, int stock) {
        // 상품 가져오고
        Product product = getProductById(productId);
        // 받은 수량으로 세팅
        product.updateProductStock(stock);
        // 해당 메서드로 알아서 상태 관리.
        setProductStatus(product);
        return new ProductResponseDto(product);
    }

    // 상태만 관리하는 메서드 분리 수량 관리는 따로.
    @Transactional
    public void setProductStatus(Product product){
        int stock = product.getStock();

        if(product.getStatus() == Status.DISCONTINUED){
            return;
        }
        if(stock <= 0){
            product.soldOutProduct();
            return;
        }
        product.onSaleProduct();
    }

    @Transactional
    public void increaseStock(Product product, int decrementCount){
        product.updateProductStock(product.getStock() + decrementCount);
        setProductStatus(product);
    }

    //입력받은 수량 만큼 감소하는 서비스
    @Transactional
    public void decreaseStock(Product product, int decrementCount) {
        int stock = Math.max(product.getStock() - decrementCount, 0);

        product.updateProductStock(stock);
        setProductStatus(product);
    }

    @Transactional
    public ProductResponseDto updateProductStatus(Long productId, UpdateProductStatusRequestDto request) {
        Product product = getProductById(productId);
        product.updateProductStatus(request.getStatus());
        return new ProductResponseDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        //삭제라는게 단종의 의미라면 이런식으로 작성 해야합니다.
        product.discontinuedProduct();
        productRepository.saveAndFlush(product);

        productRepository.delete(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품 입니다.")
        );
    }

    public Product checkProductStock(Long productId, int quantity){
        Product product = getProductById(productId);
        // 재고가 남아도 단종이면 주문 불가?
        if (product.getStatus() == Status.DISCONTINUED){
            throw new IllegalStateException("단종된 상품입니다.");
        }
        if (product.getStatus() == Status.SOLD_OUT){
            throw new IllegalStateException("품절된 상품입니다.");
        }
        if (product.getStock() < quantity){
            throw new IllegalStateException("재고가 부족합니다.");
        }
        return product;
    }
}


