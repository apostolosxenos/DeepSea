package com.xenos.deepsea.service;

import com.xenos.deepsea.domain.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticService {

    private static final String WHITESPACE = "\\s+";

    @Autowired
    private Statistic statistic;

    public void addData(Map<Integer, String> map) {
        statistic.setIndexedData(map);
    }

    public Map<String, Integer> topTenWebPagesAndHostsRequests() {

        statistic.getIndexedData()
                .values()
                .forEach(line -> statistic.getWebpagesAndHosts().add(line.split(WHITESPACE.toString())[0]));

        this.setFrequencies(statistic.getWebpagesAndHosts());

        return (HashMap<String, Integer>) statistic.getDataWithFrequencies()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10);
    }

    public void addResponseStatusCodes() {

        for (Map.Entry<Integer, String> entry : statistic.getIndexedData().entrySet()) {

            String[] parts = entry.getValue().split(WHITESPACE);

            int responseStatusCodeIndex = parts.length - 2;

            if (parts.length > 0 && parts[responseStatusCodeIndex] != null && parts[responseStatusCodeIndex].length() == 3) {
                    statistic.getResponseStatusCodes().add(parts[responseStatusCodeIndex]);
            }
        }
    }

    public void setResponsesStatusPercentage() {

        int successful = 0;
        int unsuccessful = 0;

        for (String responseCode : statistic.getResponseStatusCodes()) {
            if (responseCode.startsWith("200") || responseCode.startsWith("300"))
                ++successful;
            else
                ++unsuccessful;
        }

        int total = statistic.getResponseStatusCodes().size();

        double successfulPercentage = calculatePercentage(successful, total);
        double unsuccessfulPercentage = calculatePercentage(unsuccessful, total);

        statistic.setSuccessfulRequestsPercentage(successfulPercentage);
        statistic.setSuccessfulRequestsPercentage(unsuccessfulPercentage);
    }

    public double getSuccessfulRequestsPercentage() {
        return statistic.getSuccessfulRequestsPercentage();
    }

    public double getUnsuccessfulRequestsPercentage() {
        return statistic.getUnsuccessfulRequestsPercentage();
    }

    private void setFrequencies(List<String> list) {

        if (list == null || list.isEmpty())
            throw new IllegalArgumentException("No data in list");

        for (String str : list) {
            Integer frequency = statistic.getDataWithFrequencies().get(str);
            statistic.getDataWithFrequencies().put(str, (frequency == null) ? 1 : frequency + 1);
        }
    }

    private double calculatePercentage(double a, double b) {
        return a * 100 / b;
    }

}
