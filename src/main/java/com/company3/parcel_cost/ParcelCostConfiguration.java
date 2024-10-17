package com.company3.parcel_cost;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;

@Configuration
@PropertySource("classpath:parcelcost.properties")
public class ParcelCostConfiguration {

    @Value("${parcel.weight.kg.min.reject}")
    private Double parcelWeightKgMinReject;

    @Value("${parcel.weight.kg.min.heavy}")
    private Double parcelWeightKgMinHeavy;

    @Value("${parcel.volume.cm3.max.small}")
    private Double parcelVolumeCm3MaxSmall;

    @Value("${parcel.volume.cm3.max.medium}")
    private Double parcelVolumeCm3MaxMedium;

    @Value("${parcel.cost.multiplier.heavy}")
    private BigDecimal parcelCostMultiplierHeavy;

    @Value("${parcel.cost.multiplier.small}")
    private BigDecimal parcelCostMultiplierSmall;

    @Value("${parcel.cost.multiplier.medium}")
    private BigDecimal parcelCostMultiplierMedium;

    @Value("${parcel.cost.multiplier.large}")
    private BigDecimal parcelCostMultiplierLarge;

    @Value("${voucher.service.url}")
    private String voucherServiceUrl;

    @Value("${voucher.service.apikey}")
    private String voucherServiceApiKey;

    public Double getParcelWeightKgMinReject() {
        return parcelWeightKgMinReject;
    }

    public Double getParcelWeightKgMinHeavy() {
        return parcelWeightKgMinHeavy;
    }

    public Double getParcelVolumeCm3MaxSmall() {
        return parcelVolumeCm3MaxSmall;
    }

    public Double getParcelVolumeCm3MaxMedium() {
        return parcelVolumeCm3MaxMedium;
    }

    public BigDecimal getParcelCostMultiplierHeavy() {
        return parcelCostMultiplierHeavy;
    }

    public BigDecimal getParcelCostMultiplierSmall() {
        return parcelCostMultiplierSmall;
    }

    public BigDecimal getParcelCostMultiplierMedium() {
        return parcelCostMultiplierMedium;
    }

    public BigDecimal getParcelCostMultiplierLarge() {
        return parcelCostMultiplierLarge;
    }

    public String getVoucherServiceUrl() {
        return voucherServiceUrl;
    }

    public String getVoucherServiceApiKey() {
        return voucherServiceApiKey;
    }

}
