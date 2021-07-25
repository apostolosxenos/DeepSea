package com.xenos.deepsea;

import com.xenos.deepsea.service.GZipService;
import com.xenos.deepsea.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
        statisticService.addDataToMap(indexedData);


        // 1
        statisticService.addWebpages();
        print(statisticService.topTenWebpages());

        // 2 & 3
        statisticService.addResponseStatusCodes();
        statisticService.setResponsesStatusPercentage();
        print(statisticService.getSuccessfulRequestsPercentage());
        print(statisticService.getUnsuccessfulRequestsPercentage());

        // 4
        statisticService.clearDataWithFrequencies();
        print(statisticService.topTenUnsuccessfulRequests());

        // 5
        statisticService.clearDataWithFrequencies();
        statisticService.addHosts();
        print(statisticService.topTenHosts());


    }

    private Path getFilePath(String source) {
        return Paths.get(source);
    }

    private void print(Map<String, Integer> map) {
        map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ", " + e.getValue()));
    }

    private void print(List<String> list) {
        list.stream().forEach(e -> System.out.println(e));
    }

    private void print(double percentage) {
        System.out.println(percentage);
    }

}
