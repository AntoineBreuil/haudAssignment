package com.antoine.assignment.haud;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class HaudApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaudApplication.class, args);
    }

}
