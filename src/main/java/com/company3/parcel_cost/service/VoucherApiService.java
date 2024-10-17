package com.company3.parcel_cost.service;

import com.company3.parcel_cost.ParcelCostConfiguration;
import com.company3.parcel_cost.exception.VoucherException;
import com.company3.parcel_cost.model.VoucherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class VoucherApiService {

    private static final Logger LOG = LoggerFactory.getLogger(VoucherApiService.class);

    private static final RestClient restClient = RestClient.builder().requestFactory(restClientRequestFactory()).build();

    private final ParcelCostConfiguration parcelCostConfiguration;

    @Autowired
    public VoucherApiService(ParcelCostConfiguration parcelCostConfiguration) {
        this.parcelCostConfiguration = parcelCostConfiguration;
    }

    public VoucherResponse queryVoucherDetails(String voucher) {
        return restClient.get()
                .uri(parcelCostConfiguration.getVoucherServiceUrl() + "/{voucher}?key={apikey}",
                        voucher,
                        parcelCostConfiguration.getVoucherServiceApiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    LOG.error("Voucher API error: {}, {}, {}",
                            voucher,
                            response.getStatusCode(),
                            new String(response.getBody().readAllBytes(), UTF_8));
                    throw new VoucherException("Voucher error");
                })
                .body(VoucherResponse.class);
    }

    private static SimpleClientHttpRequestFactory restClientRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(10));
        return factory;
    }

}
