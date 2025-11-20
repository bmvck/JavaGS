package com.gs.gestaoativos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GestaoAtivosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoAtivosApplication.class, args);
	}

}

