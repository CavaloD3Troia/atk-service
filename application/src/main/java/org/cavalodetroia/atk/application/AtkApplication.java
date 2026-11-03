package org.cavalodetroia.atk.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Composition root. The domain has no annotations, so it is built by hand in {@code wiring}; the adapters in api
 * and infra are picked up by component scan.
 */
@SpringBootApplication(scanBasePackages = {
        "org.cavalodetroia.atk.application",
        "org.cavalodetroia.atk.api",
        "org.cavalodetroia.atk.infra"})
public class AtkApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AtkApplication.class, args);
    }
}
