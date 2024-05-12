package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.PagingResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ProductResponse;
import com.enigma.marketplace.MarketPlaceAPI.service.ProductService;
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
@RequestMapping(path = APIUrl.PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String id){
        ProductResponse productResponse = productService.getProductById(id);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(productResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ){
        SearchRequest request = SearchRequest.builder().direction(direction)
                .name(name).page(page).size(size).sortBy(sortBy).build();
        Page<ProductResponse> productResponses = productService.getAllProduct(request);
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
}
