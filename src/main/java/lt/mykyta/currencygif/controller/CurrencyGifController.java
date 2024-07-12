package lt.mykyta.currencygif.controller;

import lombok.RequiredArgsConstructor;
import lt.mykyta.currencygif.service.CurrencyGifService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gif")
@RequiredArgsConstructor
public class CurrencyGifController {

    private final CurrencyGifService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkExchangeRateToUSD(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getGif());
    }

}
