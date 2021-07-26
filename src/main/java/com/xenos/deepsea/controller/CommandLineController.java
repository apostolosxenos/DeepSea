package com.xenos.deepsea.controller;

import com.xenos.deepsea.service.GZipService;
import com.xenos.deepsea.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Controller
public class CommandLineController {

    @Autowired
    GZipService gzip;

    @Autowired
    private StatisticService statisticService;

    public void initFromFile(Path source) {

        System.out.println("Loading data from file... Please wait.");

        Map<Integer, String> indexedData = gzip.decompressToMap(source);
        statisticService.addDataToMap(indexedData);
        statisticService.addWebpages();
        statisticService.addResponseStatusCodes();
        statisticService.setResponsesStatusPercentage();
        statisticService.addHosts();
        statisticService.addMalformedLines(10);

        System.out.println("Finished.");
    }

    public void displayChoices() {

        System.out.println("Choose an available option to display:");
        System.out.println("1: Top 10 webpages requested by user");
        System.out.println("2: Percentage % of successful requests");
        System.out.println("3: Percentage % of unsuccessful requests");
        System.out.println("4: Top 10 unsuccessful requests");
        System.out.println("5: Top 10 hosts");
        System.out.println("6: Malformed lines");
        System.out.println("0: All of the above");
        System.out.println("'exit' to exit program");

    }

    public void promptUsersChoice() {

        Scanner in = new Scanner(System.in);

        while (true) {

            String choice = in.nextLine();

            if (choice.equalsIgnoreCase("exit")) {
                in.close();
                break;
            }

            this.execBasedOnUsersChoice(choice);
        }

        this.exitProgram();
    }

    private void execBasedOnUsersChoice(String choice) {

        if (choice == null)
            throw new IllegalArgumentException();

        switch (choice) {

            case "1":
                System.out.println("--- Top 10 Webpages ---");
                print(statisticService.topTenWebpages());
                System.out.println();
                break;
            case "2":
                System.out.println("--- Successful Requests Percentage ---");
                print(statisticService.getSuccessfulRequestsPercentage());
                System.out.println();
                break;
            case "3":
                System.out.println("--- Unsuccessful Requests Percentage ---");
                print(statisticService.getUnsuccessfulRequestsPercentage());
                System.out.println();
                break;
            case "4":
                System.out.println("--- Top 10 Unsuccessful Requests");
                print(statisticService.topTenUnsuccessfulRequests());
                System.out.println();
                break;
            case "5":
                System.out.println("--- Top 10 Hosts ---");
                print(statisticService.topTenHosts());
                System.out.println();
                break;
            case "6":
                System.out.println("--- Malformed Line Numbers, Total = " + statisticService.getMalformedLinesNumbers().size() + " ---");
                printError(statisticService.getMalformedLinesNumbers());
                System.out.println();
                break;
            case "0":
                this.execAllTasks();
                break;
            default:
                this.displayChoices();
        }

    }

    private void execAllTasks() {

        // Task 1
        print(statisticService.topTenWebpages());

        // Tasks 2 & 3
        print(statisticService.getSuccessfulRequestsPercentage());
        print(statisticService.getUnsuccessfulRequestsPercentage());

        // Task 4
        print(statisticService.topTenUnsuccessfulRequests());

        // Task 5
        print(statisticService.topTenHosts());

        // Task 6
        print(statisticService.getMalformedLinesNumbers());

        System.out.println("--- FINISHED ---");
    }

    private void exitProgram() {
        System.out.println("Exiting...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }

    private void print(Map<String, Integer> map) {
        map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ", " + e.getValue()));
    }

    private void print(List<?> list) {
        list.stream().forEach(e -> System.out.println(e));
    }

    private void printError(List<?> list) {
        list.stream().forEach(e -> System.out.println(e));
    }

    private void print(double percentage) {
        System.out.println(percentage + "%");
    }
}
