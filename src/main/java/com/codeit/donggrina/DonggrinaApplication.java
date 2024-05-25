package com.codeit.donggrina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DonggrinaApplication {

  public static void main(String[] args) {
    SpringApplication.run(DonggrinaApplication.class, args);
  }

}
