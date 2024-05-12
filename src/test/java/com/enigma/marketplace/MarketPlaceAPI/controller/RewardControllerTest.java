package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.service.RewardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardControllerTest {
    @MockBean
    private RewardService rewardService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addReward() throws Exception{
        NewRewardRequest request = NewRewardRequest.builder().name("reward").point(1).build();
        RewardResponse rewardResponse = RewardResponse.builder().id("id").rewardPoint(request.getPoint()).rewardName(request.getName()).build();
        when(rewardService.addReward(any(NewRewardRequest.class))).thenReturn(rewardResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(APIUrl.REWARD).contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RewardResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<RewardResponse>>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getRewardById() throws Exception{
        String id = "id";
        RewardResponse rewardResponse = RewardResponse.builder().id(id).rewardName("reward").rewardPoint(1).build();
        when(rewardService.getRewardById(anyString())).thenReturn(rewardResponse);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.REWARD+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RewardResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<RewardResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllReward() throws Exception{
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        List<RewardResponse> rewardResponses = List.of(RewardResponse.builder().id("id").rewardPoint(1).rewardName("reward").build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<RewardResponse> rewardResponsePage = new PageImpl<>(rewardResponses,page, rewardResponses.size());
        when(rewardService.getAllReward(any(SearchRequest.class))).thenReturn(rewardResponsePage);
        String stringRequest =objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.REWARD).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<RewardResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<List<RewardResponse>>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateReward() throws Exception{
        UpdateRewardRequest request = UpdateRewardRequest.builder().id("id").name("rewardNew").point(1).build();
        RewardResponse rewardResponse = RewardResponse.builder().id(request.getId()).rewardName(request.getName()).rewardPoint(request.getPoint()).build();
        when(rewardService.updateReward(any(UpdateRewardRequest.class))).thenReturn(rewardResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put(APIUrl.REWARD).contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RewardResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<RewardResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteById() throws Exception{
        String id = "id";
        RewardResponse rewardResponse = RewardResponse.builder().id(id).build();
        doNothing().when(rewardService).disableRewardById(anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete(APIUrl.REWARD+"/"+id).contentType(MediaType.APPLICATION_JSON_VALUE).content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RewardResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<RewardResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, response.getMessage());
                } );
    }
}
