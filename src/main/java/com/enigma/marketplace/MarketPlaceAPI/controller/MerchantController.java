package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.MerchantResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.PagingResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ProductResponse;
import com.enigma.marketplace.MarketPlaceAPI.security.AuthenticatedUser;
import com.enigma.marketplace.MarketPlaceAPI.service.MerchantService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.MERCHANT)
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER') or @authenticatedUser.hasMerchantId(#id)")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<MerchantResponse>> getMerchantById(@PathVariable String id){
        MerchantResponse merchantResponse = merchantService.getMerchantById(id);
        CommonResponse<MerchantResponse> response = CommonResponse.<MerchantResponse>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(merchantResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<MerchantResponse>>> getAllMerchant(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ){
        SearchRequest request = SearchRequest.builder().direction(direction)
                .name(name).page(page).size(size).sortBy(sortBy).build();
        Page<MerchantResponse> merchants = merchantService.getAllMerchant(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(merchants.getTotalPages())
                .totalElement(merchants.getTotalElements())
                .page(merchants.getPageable().getPageNumber()+1)
                .size(merchants.getPageable().getPageSize())
                .hasNext(merchants.hasNext())
                .hasPrevious(merchants.hasPrevious()).build();
        CommonResponse<List<MerchantResponse>> response = CommonResponse.<List<MerchantResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(merchants.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasMerchantId(#id)")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> disableMerchantById(@PathVariable String id){
        merchantService.disableById(id);
        CommonResponse response = CommonResponse.builder().statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasMerchantId(#id)")
    @PostMapping(path = "/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> addProduct(@PathVariable String id, @RequestBody NewProductRequest request){
        ProductResponse productResponse = merchantService.addProduct(id, request);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(productResponse).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER') or @authenticatedUser.hasMerchantId(#id)")
    @GetMapping(path = "/{id}/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String id, @PathVariable String productId){
        ProductResponse productResponse = merchantService.getProductById(id, productId);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(productResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER') or @authenticatedUser.hasMerchantId(#id)")
    @GetMapping(path = "/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @PathVariable String id
    ){
        SearchRequest request = SearchRequest.builder().direction(direction)
                .name(name).page(page).size(size).sortBy(sortBy).build();
        Page<ProductResponse> productResponses = merchantService.getAllProduct(id, request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(productResponses.getTotalPages())
                .totalElement(productResponses.getTotalElements())
                .page(productResponses.getPageable().getPageNumber()+1)
                .size(productResponses.getPageable().getPageSize())
                .hasNext(productResponses.hasNext())
                .hasPrevious(productResponses.hasPrevious()).build();
        CommonResponse<List<ProductResponse>> response = CommonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(productResponses.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasMerchantId(#id)")
    @PutMapping(path = "/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest request){
        ProductResponse productResponse = merchantService.updateProduct(id, request);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(productResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasMerchantId(#id)")
    @DeleteMapping(path = "/{id}/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> disableProductById(@PathVariable String id, @PathVariable String productId){
        merchantService.disableProductById(id, productId);
        CommonResponse response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }
}
