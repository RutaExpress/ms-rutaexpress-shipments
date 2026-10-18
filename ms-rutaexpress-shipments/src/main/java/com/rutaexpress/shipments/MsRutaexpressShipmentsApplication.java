package com.rutaexpress.shipments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Microservicio de envíos: CRUD, estados y productor de comandos RabbitMQ
@SpringBootApplication
public class MsRutaexpressShipmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRutaexpressShipmentsApplication.class, args);
	}

}
