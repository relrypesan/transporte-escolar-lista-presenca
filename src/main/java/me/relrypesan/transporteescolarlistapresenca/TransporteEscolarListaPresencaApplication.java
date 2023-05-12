package me.relrypesan.transporteescolarlistapresenca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TransporteEscolarListaPresencaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransporteEscolarListaPresencaApplication.class, args);
	}

}
