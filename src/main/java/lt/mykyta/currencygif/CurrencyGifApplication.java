package lt.mykyta.currencygif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CurrencyGifApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyGifApplication.class, args);
	}

}
