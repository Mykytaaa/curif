package lt.mykyta.currencygif.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gif", url = "${external-api.gif-link-api}")
public interface GifClient {
    @GetMapping()
    ResponseEntity<String> getRandomGif(@RequestParam("api_key") String apiKey,
                                        @RequestParam("tag") String tag);
}
