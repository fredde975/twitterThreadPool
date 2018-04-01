package com.example.twitter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
public class Configuration {

    @Bean(name = "ConcurrentExecutorService")
    public ExecutorService excutorService() {
        return Executors.newFixedThreadPool(3);
    }

}