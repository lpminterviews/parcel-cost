package com.company3.parcel_cost.service;

import com.company3.parcel_cost.ParcelCostConfiguration;
import com.company3.parcel_cost.exception.ParcelDimensionException;
import com.company3.parcel_cost.model.ParcelDimensions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParcelCostServiceTest {

    private ParcelCostConfiguration parcelCostConfiguration = mock(ParcelCostConfiguration.class);
    private VoucherService voucherService = mock(VoucherService.class);

    private ParcelCostService parcelCostService = new ParcelCostService(voucherService, parcelCostConfiguration);

    @BeforeEach
    void setUp() {
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
    public void testReject() {
        ParcelDimensions dimensions = new ParcelDimensions(51d, 10d, 10d, 10d);

        assertThrows(ParcelDimensionException.class,
                () -> parcelCostService.calculateCost(dimensions, null));
    }

    @Test
    public void testHeavy() {
        Double weight = 50d;
        ParcelDimensions dimensions = new ParcelDimensions(weight, 10d, 10d, 10d);
        BigDecimal cost = parcelCostService.calculateCost(dimensions, null);
        assertThat(cost).isEqualByComparingTo(
                BigDecimal.valueOf(weight).multiply(parcelCostConfiguration.getParcelCostMultiplierHeavy())
        );
    }

    @Test
    public void testSmallParcel() {
        ParcelDimensions dimensions = new ParcelDimensions(5d, 10d, 10d, 10d);
        BigDecimal cost = parcelCostService.calculateCost(dimensions, null);
        assertThat(cost).isEqualByComparingTo(
                BigDecimal.valueOf(10 * 10 * 10).multiply(parcelCostConfiguration.getParcelCostMultiplierSmall())
        );
    }

    @Test
    public void testMediumParcel() {
        ParcelDimensions dimensions = new ParcelDimensions(5d, 20d, 10d, 10d);
        BigDecimal cost = parcelCostService.calculateCost(dimensions, null);
        assertThat(cost).isEqualByComparingTo(
                BigDecimal.valueOf(20 * 10 * 10).multiply(parcelCostConfiguration.getParcelCostMultiplierMedium())
        );
    }

    @Test
    public void testLargeParcel() {
        ParcelDimensions dimensions = new ParcelDimensions(5d, 20d, 20d, 20d);
        BigDecimal cost = parcelCostService.calculateCost(dimensions, null);
        assertThat(cost).isEqualByComparingTo(
                BigDecimal.valueOf(20 * 20 * 20).multiply(parcelCostConfiguration.getParcelCostMultiplierLarge())
        );
    }

    @Test
    public void testVoucherDiscount() {
        BigDecimal discount = BigDecimal.TEN;
        when(voucherService.getVoucherDiscount(anyString())).then(e -> discount);

        ParcelDimensions dimensions = new ParcelDimensions(5d, 20d, 20d, 20d);
        BigDecimal cost = parcelCostService.calculateCost(dimensions, "TESTCODE");
        assertThat(cost).isEqualByComparingTo(
                BigDecimal.valueOf(20 * 20 * 20).multiply(parcelCostConfiguration.getParcelCostMultiplierLarge())
                        .subtract(discount)
        );
    }

}