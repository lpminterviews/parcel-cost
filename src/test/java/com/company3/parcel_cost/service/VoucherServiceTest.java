package com.company3.parcel_cost.service;

import com.company3.parcel_cost.model.VoucherResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VoucherServiceTest {

    private VoucherApiService voucherApiService = mock(VoucherApiService.class);
    private VoucherService voucherService = new VoucherService(voucherApiService);

    @Test
    public void testExpired() {
        VoucherResponse expired = new VoucherResponse("test", 10d, "2024-01-01");
        when(voucherApiService.queryVoucherDetails("test")).then(e -> expired);

        BigDecimal discount = voucherService.getVoucherDiscount("test");
        assertThat(discount).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void testNotExpired() {
        VoucherResponse notExpired = new VoucherResponse("test", 10d, "2100-01-01");
        when(voucherApiService.queryVoucherDetails("test")).then(e -> notExpired);

        BigDecimal discount = voucherService.getVoucherDiscount("test");
        assertThat(discount).isEqualByComparingTo(BigDecimal.TEN);
    }

}