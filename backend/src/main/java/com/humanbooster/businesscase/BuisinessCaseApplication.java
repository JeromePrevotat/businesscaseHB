package com.humanbooster.businesscase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class BuisinessCaseApplication {

	public static void main(String[] args) {        
        try {
            System.out.println("===== STARTING APP =====\n");
            SpringApplication.run(BuisinessCaseApplication.class, args);
            System.out.println("===== APP RUNNING =====\n");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
