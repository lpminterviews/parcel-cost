package com.company3.parcel_cost.service;

import com.company3.parcel_cost.model.VoucherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class VoucherService {

    private static final RestClient restClient = RestClient.builder().requestFactory(restClientRequestFactory()).build();

    private final VoucherApiService voucherApiService;

    @Autowired
    public VoucherService(VoucherApiService voucherApiService) {
        this.voucherApiService = voucherApiService;
    }

    public BigDecimal getVoucherDiscount(String voucher) {
        VoucherResponse voucherResponse = voucherApiService.queryVoucherDetails(voucher);

        String expiry = voucherResponse.expiry();
        LocalDate expiryDateTime = LocalDate.parse(expiry, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (LocalDate.now().isBefore(expiryDateTime)) {
            return BigDecimal.valueOf(voucherResponse.discount());
        }

        return BigDecimal.ZERO;
    }

    private static SimpleClientHttpRequestFactory restClientRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(10));
        return factory;
    }

}
