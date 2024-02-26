package com.swiggy.wallet.controller;

import com.swiggy.wallet.dto.ConvertorRequest;
import com.swiggy.wallet.grpcClient.CurrencyConversionClient;
import currencyconversion.ConvertRequest;
import currencyconversion.ConvertResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class ConversionTestController {

    private final CurrencyConversionClient conversionClient;

    public ConversionTestController(CurrencyConversionClient conversionClient) {
        this.conversionClient = conversionClient;
    }

    @PostMapping("/convert")
    public ResponseEntity<String> currencyConvertor(@RequestBody ConvertorRequest request) {
        ConvertResponse response = conversionClient.convertCurrency(request.getBaseCurrency(), request.getTargetCurrency(), request.getAmount());
        return new ResponseEntity<>("response "+response.getConvertedAmount()+" currency"+response.getCurrency(), HttpStatus.OK);
    }
}
