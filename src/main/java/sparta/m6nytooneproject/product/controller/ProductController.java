package sparta.m6nytooneproject.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.product.dto.*;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.ProductSort;
import sparta.m6nytooneproject.product.entity.Status;
import sparta.m6nytooneproject.product.service.ProductService;
import sparta.m6nytooneproject.security.CustomUserDetails;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ApiResponseDto<ProductResponseDto> createProduct(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProductRequestDto request
    ) {
        ProductResponseDto result = productService.createProduct(userDetails, request);

        return ApiResponseDto.success(HttpStatus.CREATED, result);
    }

    @GetMapping
    public ApiResponseDto<ProductResponseDto> getAllProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false, defaultValue = "CREATED_AT") ProductSort productSort
    ) {
        return ApiResponseDto.pagination(HttpStatus.OK,
                productService.getAllProducts(page, size, productSort, productName, category, status), "정상적으로 조회 되었습니다");
    }

    @GetMapping("/{productId}")
    public ApiResponseDto<GetOneProductResponseDto> getOneProduct(
            @PathVariable Long productId
    ) {
        return ApiResponseDto.success(HttpStatus.OK, productService.getOneProduct(productId));
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ApiResponseDto<ProductResponseDto> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequestDto request
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponseDto.success(HttpStatus.OK, productService.updateProduct(productId, request));
    }

    @PatchMapping("/{productId}/stocks")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ApiResponseDto<ProductResponseDto> updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequestDto request
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponseDto.success(
                HttpStatus.OK,
                productService.updateProductStock(productId, request.getStock()));

    }

    @PatchMapping("/{productId}/status")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ApiResponseDto<ProductResponseDto> updateProductStatus(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStatusRequestDto request
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponseDto.success(
                HttpStatus.OK,
                productService.updateProductStatus(productId, request));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ApiResponseDto<Void> deleteProduct(
            @PathVariable Long productId
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        productService.deleteProduct(productId);
        return ApiResponseDto.successWithNoContent();
    }
}
