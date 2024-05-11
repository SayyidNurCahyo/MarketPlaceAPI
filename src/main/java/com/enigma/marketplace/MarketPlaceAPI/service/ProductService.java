package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ProductResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Merchant;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse addProduct(Merchant merchant, NewProductRequest request);
    ProductResponse getProductById(String productId);
    ProductResponse getProductById(String merchantId, String productId);
    Page<ProductResponse> getAllProduct(String merchantId, SearchRequest request);
    Page<ProductResponse> getAllProduct(SearchRequest request);
    ProductResponse updateProduct(String merchantId, UpdateProductRequest request);
    void disableProductById(String merchantId, String id);
}
