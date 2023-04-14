package com.tutorial.api.Spring.boot.demo.database;

import com.tutorial.api.Spring.boot.demo.models.Product;
import com.tutorial.api.Spring.boot.demo.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Database {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA =  new Product( "Macbook pro M1", 2020, 2400.0,"");
                Product productB = new Product( "iPhone 13", 2020,1200.0,"");
                logger.info("insert data " + productRepository.save(productA));
                logger.info("insert data " + productRepository.save(productB));
            }
        };
    }
}
