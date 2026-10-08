package br.com.boleiroOn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BoleiroOnApplication {

	public static void main(String[] args) {
		// Headless mode para geração de relatórios (Excel/PDF) em servidor sem display
		System.setProperty("java.awt.headless", "true");
		SpringApplication.run(BoleiroOnApplication.class, args);
	}

}
