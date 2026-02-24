package sparta.m6nytooneproject.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.product.dto.*;
import sparta.m6nytooneproject.product.entity.Category;
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
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProductRequestDto request
    ) {
        ProductResponseDto result = productService.createProduct(userDetails, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<ProductResponseDto>>> getAllProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.getAllProducts(pageable, productName, category, status)));
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ResponseEntity<ApiResponseDto<GetOneProductResponseDto>> getOneProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.getOneProduct(productId)));
    }

    @PatchMapping ("/{productId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.updateProduct(userDetails, productId, request)));
    }

    @PatchMapping("/{productId}/stocks")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity
                .ok(ApiResponseDto.success(productService.updateProductStock(userDetails, productId, request.getStock())));
    }

    @PatchMapping("/{productId}/status")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductStatus(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStatusRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.updateProductStatus(userDetails, productId, request)));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SUPER','OPER')")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        productService.deleteProduct(userDetails, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}
