package lt.mykyta.currencygif.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency", url = "${external-api.open-exchange-rates-link-api}")
public interface CurrencyClient {
    @GetMapping("/historical/{date}.json")
    ResponseEntity<String> checkExchangeRateToUSD(@PathVariable("date") String date,
                                                  @RequestParam("app_id") String appId,
                                                  @RequestParam("symbols") String symbols);
}
