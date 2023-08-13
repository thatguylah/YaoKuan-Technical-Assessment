package com.onlinebookstore.config;

import com.onlinebookstore.util.DBSeederService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

@Component
public class DBSeederRunner implements CommandLineRunner {

    private final DBSeederService seederService;

    @Value("${seed.database}")
    private boolean seedDatabase;

    public DBSeederRunner(DBSeederService seederService) {
        this.seederService = seederService;
    }

    @Override
    public void run(String... args) {
        if (seedDatabase) {
            seederService.seedDatabase();
        }
    }
}
