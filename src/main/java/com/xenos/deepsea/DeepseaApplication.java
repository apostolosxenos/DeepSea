package com.xenos.deepsea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class DeepseaApplication implements CommandLineRunner {

    @Autowired
    GZipService gzip;

    public static void main(String[] args) {
        SpringApplication.run(DeepseaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Path source = Paths.get("input/NASA_access_log_Aug95.gz");
        String data = gzip.decompressToString(source);


    }
}
