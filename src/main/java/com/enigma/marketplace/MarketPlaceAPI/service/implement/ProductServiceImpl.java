package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ProductResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Merchant;
import com.enigma.marketplace.MarketPlaceAPI.entity.Product;
import com.enigma.marketplace.MarketPlaceAPI.repository.ProductRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.ProductService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse addProduct(Merchant merchant, NewProductRequest request) {
        validationUtil.validate(request);
        Product product = Product.builder().name(request.getName()).price(request.getPrice())
                .stock(request.getStock()).merchant(merchant).isActive(true).build();
        Product productSaved = productRepository.saveAndFlush(product);
        return convertToProductResponse(productSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductById(String productId) {
        return convertToProductResponse(productRepository.findProductById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductById(String merchantId, String productId) {
        return convertToProductResponse(productRepository.findProductById(productId, merchantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAllProduct(String merchantId, SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!= null) return convertToPageProductResponse(productRepository.findProduct(merchantId, "%"+request.getName()+"%", page));
        else return convertToPageProductResponse(productRepository.findAllProduct(merchantId, page));
    }


    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAllProduct(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!= null) return convertToPageProductResponse(productRepository.findProduct("%"+request.getName()+"%", page));
        else return convertToPageProductResponse(productRepository.findAllProduct(page));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse updateProduct(String merchantId, UpdateProductRequest request) {
        validationUtil.validate(request);
        Product product = productRepository.findProductById(request.getId(), merchantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        productRepository.saveAndFlush(product);
        return convertToProductResponse(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableProductById(String merchantId, String id) {
        Product product = productRepository.findProductById(id, merchantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setIsActive(false);
        productRepository.saveAndFlush(product);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> getAllProduct(String merchantId) {
        return productRepository.findAllProduct(merchantId);
    }

    private ProductResponse convertToProductResponse(Product product){
        return ProductResponse.builder().id(product.getId())
                .stock(product.getStock()).price(product.getPrice())
                .name(product.getName()).build();
    }


    private Page<ProductResponse> convertToPageProductResponse(Page<Product> products){
        return products.map(this::convertToProductResponse);
    }
}
