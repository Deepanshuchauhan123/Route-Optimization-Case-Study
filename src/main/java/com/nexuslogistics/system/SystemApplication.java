package com.nexuslogistics.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        NexusMain resource = new NexusMain();
        resource.findShortestPathToDeliver();
    }
}
