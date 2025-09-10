package de.paulwunsch.bund;

import org.springframework.boot.SpringApplication;

public class TestBundApplication {

    public static void main(String[] args) {
        SpringApplication.from(BundApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
