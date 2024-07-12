package lt.mykyta.currencygif.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lt.mykyta.currencygif.CurrencyGifApplication;
import lt.mykyta.currencygif.controller.CurrencyGifController;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = CurrencyGifApplication.class)
@WireMockTest
@ExtendWith(SoftAssertionsExtension.class)
public class CurrencyGifServiceTest {

    @Autowired
    private CurrencyGifController controller;

    @RegisterExtension
    private final static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Value("${external-api.current-date}")
    private String currentDate;

    private final String successfulOpenExchangeResponse = """
            {
                "disclaimer": "Usage subject to terms: https://openexchangerates.org/terms",
                "license": "https://openexchangerates.org/license",
                "timestamp": 1720396799,
                "base": "USD",
                "rates": {
                    "EUR": 0.923725
                }
            }
            """;

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("external-api.open-exchange-rates-link-api", wireMockExtension::baseUrl);
    }

    @Test
    void testCurrencyTheSame(SoftAssertions softly) {

        String a = """
                {
                    "base": "USD",
                    "rates": {
                        "EUR": 0.923725
                    }
                }
                """;
        wireMockExtension.stubFor(get(urlPathMatching("/historical/.*.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(a))
        );

        String result = controller.checkExchangeRateToUSD().getBody();

        softly.assertThat(result).isNotNull();
    }
}
