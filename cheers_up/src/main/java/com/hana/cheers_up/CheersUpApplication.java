package com.hana.cheers_up;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CheersUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheersUpApplication.class, args);
    }

}
