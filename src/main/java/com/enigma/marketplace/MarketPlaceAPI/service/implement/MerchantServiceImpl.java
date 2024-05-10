package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.MerchantResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ProductResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Merchant;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import com.enigma.marketplace.MarketPlaceAPI.repository.MerchantRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.MerchantService;
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
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addMerchant(Merchant merchant) {
        merchantRepository.saveAndFlush(merchant);
    }

    @Transactional(readOnly = true)
    @Override
    public MerchantResponse getMerchantById(String id) {
        Merchant merchant = merchantRepository.findByIdMerchant(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Merchant Not Found"));
        return convertToMerchantResponse(merchant);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MerchantResponse> getAllMerchant(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null){
            Page<Merchant> merchant = merchantRepository.findMerchant("%"+request.getName()+"%", page);
            return convertToPageMerchantResponse(merchant);
        }else {
            return convertToPageMerchantResponse(merchantRepository.findAllMerchant(page));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableById(String id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Merchant Not Found"));
        UserAccount account = merchant.getUserAccount();
        account.setIsEnable(false);
        merchant.setUserAccount(account);
        merchantRepository.saveAndFlush(merchant);
    }

    private MerchantResponse convertToMerchantResponse(Merchant merchant){
        List<ProductResponse> productResponses = merchant.getProducts().stream().map(product ->
                ProductResponse.builder().id(product.getId()).name(product.getName())
                    .price(product.getPrice()).build()).toList();
        return MerchantResponse.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .products(productResponses)
                .build();
    }

    private Page<MerchantResponse> convertToPageMerchantResponse(Page<Merchant> merchant){
        return merchant.map(this::convertToMerchantResponse);
    }
}
