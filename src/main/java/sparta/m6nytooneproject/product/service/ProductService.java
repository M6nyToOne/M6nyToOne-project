package sparta.m6nytooneproject.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.m6nytooneproject.product.dto.GetOneProductResponseDto;
import sparta.m6nytooneproject.product.dto.ProductRequestDto;
import sparta.m6nytooneproject.product.dto.ProductResponseDto;
import sparta.m6nytooneproject.product.entity.Product;
import sparta.m6nytooneproject.product.enums.Category;
import sparta.m6nytooneproject.product.enums.Status;
import sparta.m6nytooneproject.product.repository.ProductRepository;
import sparta.m6nytooneproject.user.entity.User;
import sparta.m6nytooneproject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProductResponseDto createProduct(Long userId, ProductRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("없는 유저 입니다.")
        );

        Product product = new Product(request.getProductName(), request.getCategory(), request.getPrice(), request.getStock(), request.getStatus(), user);
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
        return productRepository.findAll(pageable)
                .map(ProductResponseDto::new);
    }

    public GetOneProductResponseDto getOneProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("없는 상품 입니다.")
        );

        return new GetOneProductResponseDto(product);
    }
}

