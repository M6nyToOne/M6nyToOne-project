package sparta.m6nytooneproject.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.product.dto.*;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.Status;
import sparta.m6nytooneproject.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/users/{userId}/products")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
            @PathVariable Long userId,
            @Valid @RequestBody ProductRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(productService.createProduct(userId, request)));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponseDto<Page<ProductResponseDto>>> getAllProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.getAllProducts(pageable, productName, category, status)));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponseDto<GetOneProductResponseDto>> getOneProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.getOneProduct(productId)));
    }

    @PatchMapping ("/products/{productId}")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequestDto request
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.updateProduct(productId, request)));
    }

    @PatchMapping("/products/{productId}/stocks")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequestDto request
    ){
        return ResponseEntity
                .ok(ApiResponseDto.success(productService.updateProductStock(productId, request.getStock())));
    }

    @PatchMapping("/products/{productId}/status")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductStatus(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStatusRequestDto request
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.updateProductStatus(productId, request)));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}
