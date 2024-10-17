package com.company3.parcel_cost.controller;

import com.company3.parcel_cost.exception.ParcelDimensionException;
import com.company3.parcel_cost.exception.VoucherException;
import com.company3.parcel_cost.model.ParcelDimensions;
import com.company3.parcel_cost.model.RestResponse;
import com.company3.parcel_cost.service.ParcelCostService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ParcelCostController {

    @Autowired
    private ParcelCostService parcelCostService;

    @GetMapping(value = "/parcelcost",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateParcelCost(
            @RequestParam("weight") Double weight,
            @RequestParam("height") Double height,
            @RequestParam("width") Double width,
            @RequestParam("length") Double length,
            @Nullable @RequestParam("voucher") String voucher) {
        validateDimensions(weight, height, width, length);

        BigDecimal cost = parcelCostService.calculateCost(new ParcelDimensions(weight, height, width, length), voucher);

        return ResponseEntity
                .ok()
                .body(new RestResponse("success", cost));
    }

    private static void validateDimensions(Double... dimensions) {
        for (Double dimension : dimensions) {
            if (dimension == null || dimension <= 0) {
                throw new ParcelDimensionException("Parcel dimension is invalid");
            }
        }
    }

    @ExceptionHandler(ParcelDimensionException.class)
    public ResponseEntity<?> handleParcelDimensionException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new RestResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(VoucherException.class)
    public ResponseEntity<?> handleVoucherException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new RestResponse("error", ex.getMessage()));
    }

}
