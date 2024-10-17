package com.company3.parcel_cost.controller;

import com.company3.parcel_cost.ParcelCostConfiguration;
import com.company3.parcel_cost.exception.VoucherException;
import com.company3.parcel_cost.model.VoucherResponse;
import com.company3.parcel_cost.service.VoucherApiService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@SpringBootTest
@AutoConfigureMockMvc
class ParcelCostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherApiService voucherApiService;
    @MockBean
    private ParcelCostConfiguration parcelCostConfiguration;

    @BeforeEach
    void setUp() {
        when(voucherApiService.queryVoucherDetails(anyString())).thenReturn(new VoucherResponse("testcode", 1d, "2100-12-31"));

        when(parcelCostConfiguration.getParcelWeightKgMinReject()).then(e -> 50d);
        when(parcelCostConfiguration.getParcelWeightKgMinHeavy()).then(e -> 10d);
        when(parcelCostConfiguration.getParcelVolumeCm3MaxSmall()).then(e -> 1500d);
        when(parcelCostConfiguration.getParcelVolumeCm3MaxMedium()).then(e -> 2500d);

        when(parcelCostConfiguration.getParcelCostMultiplierHeavy()).then(e -> BigDecimal.valueOf(20d));
        when(parcelCostConfiguration.getParcelCostMultiplierSmall()).then(e -> BigDecimal.valueOf(0.03d));
        when(parcelCostConfiguration.getParcelCostMultiplierMedium()).then(e -> BigDecimal.valueOf(0.04d));
        when(parcelCostConfiguration.getParcelCostMultiplierLarge()).then(e -> BigDecimal.valueOf(0.05d));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testComputedCost() throws Exception {
        String query = String.format("/parcelcost?weight=%s&height=%s&width=%s&length=%s&voucher=%s", 10, 10, 10, 10, "testvoucher");
        this.mockMvc.perform(get(query)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.body", is(29.0d)))
                .andReturn();
    }

    @Test
    public void testRejectedParcel() throws Exception {
        String query = String.format("/parcelcost?weight=%s&height=%s&width=%s&length=%s&voucher=%s", 150, 10, 10, 10, "testvoucher");
        this.mockMvc.perform(get(query)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.body", is("Parcel weight exceeds 50.0kg")))
                .andReturn();
    }

    @Test
    public void testInvalidDimensions() throws Exception {
        String query = String.format("/parcelcost?weight=%s&height=%s&width=%s&length=%s&voucher=%s", 0, 0, 0, 0, "testvoucher");
        this.mockMvc.perform(get(query)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.body", is("Parcel dimension is invalid")))
                .andReturn();
    }

    @Test
    public void testInvalidVoucher() throws Exception {
        when(voucherApiService.queryVoucherDetails(anyString())).thenThrow(new VoucherException("Voucher error"));

        String query = String.format("/parcelcost?weight=%s&height=%s&width=%s&length=%s&voucher=%s", 50, 10, 10, 10, "mocked");
        this.mockMvc.perform(get(query)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.body", is("Voucher error")))
                .andReturn();
    }

}