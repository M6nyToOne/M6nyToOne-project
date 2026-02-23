package sparta.m6nytooneproject.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.m6nytooneproject.global.AuthConstants;
import sparta.m6nytooneproject.global.dto.ApiResponseDto;
import sparta.m6nytooneproject.global.dto.SessionUserDto;
import sparta.m6nytooneproject.product.dto.*;
import sparta.m6nytooneproject.product.entity.Category;
import sparta.m6nytooneproject.product.entity.Status;
import sparta.m6nytooneproject.product.service.ProductService;

@RestController("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser,
            @Valid @RequestBody ProductRequestDto request
    ) {
        ProductResponseDto result = productService.createProduct(sessionUser, request);

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
    public ResponseEntity<ApiResponseDto<GetOneProductResponseDto>> getOneProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.getOneProduct(productId)));
    }

    @PatchMapping ("/{productId}")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.updateProduct(sessionUser, productId, request)));
    }

    @PatchMapping("/{productId}/stocks")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ){
        return ResponseEntity
                .ok(ApiResponseDto.success(productService.updateProductStock(sessionUser, productId, request.getStock())));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductStatus(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStatusRequestDto request,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(productService.updateProductStatus(sessionUser, productId, request)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable Long productId,
            @SessionAttribute(name = AuthConstants.LOGIN_USER) SessionUserDto sessionUser
    ) {
        productService.deleteProduct(sessionUser, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.successWithNoContent());
    }
}
