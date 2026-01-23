package com.dex.srp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SkillRecapProjectApplication {

    public static void main(String[] args) {
        log.info(">SkillRecapProject starting...");
        SpringApplication.run(SkillRecapProjectApplication.class, args);
    }

}
