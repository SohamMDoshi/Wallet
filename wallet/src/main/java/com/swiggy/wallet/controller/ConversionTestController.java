package com.swiggy.wallet.controller;

import com.swiggy.wallet.dto.ConvertorRequest;
import com.swiggy.wallet.grpcClient.CurrencyConversionClient;
import currencyconversion.ConvertResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.swiggy.wallet.grpcClient.CurrencyConversionClient.convertCurrency;

@RestController
@RequestMapping("/test")
public class ConversionTestController {


    @PostMapping("/convert")
    public ResponseEntity<String> currencyConvertor(@RequestBody ConvertorRequest request) {
        ConvertResponse response = convertCurrency(request.getBaseCurrency(), request.getTargetCurrency(), request.getAmount());
        return new ResponseEntity<>("response "+response.getConvertedAmount()+" currency"+response.getCurrency(), HttpStatus.OK);
    }
}
