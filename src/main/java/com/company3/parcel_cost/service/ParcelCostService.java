package com.company3.parcel_cost.service;

import com.company3.parcel_cost.ParcelCostConfiguration;
import com.company3.parcel_cost.exception.ParcelDimensionException;
import com.company3.parcel_cost.model.ParcelDimensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Service
public class ParcelCostService {

    private final VoucherService voucherService;
    private final ParcelCostConfiguration parcelCostConfiguration;

    @Autowired
    public ParcelCostService(VoucherService voucherService, ParcelCostConfiguration parcelCostConfiguration) {
        this.voucherService = voucherService;
        this.parcelCostConfiguration = parcelCostConfiguration;
    }

    public BigDecimal calculateCost(ParcelDimensions parcelDimensions, String voucher) {
        BigDecimal baseCost = calculateBaseCost(parcelDimensions);
        if (!ObjectUtils.isEmpty(voucher)) {
            BigDecimal discount = voucherService.getVoucherDiscount(voucher);
            // assume voucher discount is subtraction, not percentage
            return baseCost.subtract(discount);
        }
        return baseCost;
    }

    private BigDecimal calculateBaseCost(ParcelDimensions parcelDimensions) {
        if (parcelDimensions.weight() > parcelCostConfiguration.getParcelWeightKgMinReject()) {
            throw new ParcelDimensionException("Parcel weight exceeds " + parcelCostConfiguration.getParcelWeightKgMinReject() + "kg");
        }
        if (parcelDimensions.weight() > parcelCostConfiguration.getParcelWeightKgMinHeavy()) {
            return BigDecimal.valueOf(parcelDimensions.weight()).multiply(parcelCostConfiguration.getParcelCostMultiplierHeavy());
        }

        double parcelVolume = calculateVolume(parcelDimensions);
        if (parcelVolume < parcelCostConfiguration.getParcelVolumeCm3MaxSmall()) {
            return BigDecimal.valueOf(parcelVolume).multiply(parcelCostConfiguration.getParcelCostMultiplierSmall());
        }
        if (parcelVolume < parcelCostConfiguration.getParcelVolumeCm3MaxMedium()) {
            return BigDecimal.valueOf(parcelVolume).multiply(parcelCostConfiguration.getParcelCostMultiplierMedium());
        }
        return BigDecimal.valueOf(parcelVolume).multiply(parcelCostConfiguration.getParcelCostMultiplierLarge());
    }

    private static double calculateVolume(ParcelDimensions parcelDimensions) {
        return parcelDimensions.height() * parcelDimensions.length() * parcelDimensions.width();
    }

}
