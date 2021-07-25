package com.xenos.deepsea;

import com.xenos.deepsea.service.GZipService;
import com.xenos.deepsea.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@SpringBootApplication
public class DeepseaApplication implements CommandLineRunner {

    @Autowired
    GZipService gzip;

    @Autowired
    StatisticService statisticService;

    public static void main(String[] args) {
        SpringApplication.run(DeepseaApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Path source;

        if (args.length == 0)
            source = getFilePath("input/NASA_access_log_Aug95.gz");
        else
            source = getFilePath(args[0]);

        Map<Integer, String> indexedData = gzip.decompressToMap(source);
        statisticService.addData(indexedData);


    }

    private Path getFilePath(String source) {
        return Paths.get(source);
    }


}
