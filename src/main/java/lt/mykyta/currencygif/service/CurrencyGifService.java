package lt.mykyta.currencygif.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lt.mykyta.currencygif.client.CurrencyClient;
import lt.mykyta.currencygif.client.GifClient;
import lt.mykyta.currencygif.model.ExchangeRateResponse;
import lt.mykyta.currencygif.model.GifResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyGifService {

    private final CurrencyClient currencyClient;
    private final GifClient gifClient;

    @Value("${external-api.open-exchange-rates-api-key}")
    private String openExchangeRatesApiKey;

    @Value("${external-api.gif-api-key}")
    private String gifApiKey;

    @Value("${external-api.currency}")
    private String currency;

    @Value("${external-api.current-date}")
    private String currentDate;

    @Value("${external-api.yesterday}")
    private String yesterday;

    public String getGif() {
        ObjectMapper objectMapper = new ObjectMapper();

        double currencyToday;
        double currencyYesterday;
        try {
            currencyToday = objectMapper.readValue(currencyClient.checkExchangeRateToUSD(currentDate, openExchangeRatesApiKey, currency).getBody(),
                    ExchangeRateResponse.class).getRates().get(currency);

            currencyYesterday = objectMapper.readValue(currencyClient.checkExchangeRateToUSD(yesterday, openExchangeRatesApiKey, currency).getBody(),
                    ExchangeRateResponse.class).getRates().get(currency);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        boolean isHigher = currencyToday - currencyYesterday < 0; // the boolean is true then it's poor

        if(!isHigher) {
            try {
                JsonNode rootNode = objectMapper.readTree(gifClient.getRandomGif(gifApiKey, "rich").getBody());
                JsonNode dataNode = rootNode.path("data");

                GifResponse gifResponse = objectMapper.treeToValue(dataNode, GifResponse.class);
                return gifResponse.getEmbedUrl();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            JsonNode rootNode = objectMapper.readTree(gifClient.getRandomGif(gifApiKey, "poor").getBody());
            JsonNode dataNode = rootNode.path("data");

            GifResponse gifResponse = objectMapper.treeToValue(dataNode, GifResponse.class);
            return gifResponse.getEmbedUrl();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
