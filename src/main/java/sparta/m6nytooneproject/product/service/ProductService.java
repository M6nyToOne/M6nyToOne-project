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

        Page<Product> products;
        if (productName != null && category == null && status == null) {
            // 검색 키워드 상품명 조회
            products = productRepository.findByProductNameContaining(pageable, productName);
        } else if () {
            products = productRepository.findAll(pageable);
        }

        return products.map(product -> new ProductResponseDto(product)
        );
    }

    public GetOneProductResponseDto getOneProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("없는 상품 입니다.")
        );

        return new GetOneProductResponseDto(product);
    }
}

