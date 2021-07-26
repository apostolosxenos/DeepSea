package com.xenos.deepsea.service;

import com.xenos.deepsea.domain.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private static final String WHITESPACE = "\\s+";
    private Map<Integer, String> indexedData;

    @Autowired
    private Statistic statistic;

    public StatisticService() {
        this.indexedData = new HashMap<>();
    }

    public void addDataToMap(Map<Integer, String> map) {
        this.indexedData.putAll(map);
    }

    public void addWebpages() {
        this.indexedData
                .values()
                .forEach(line -> statistic.getWebpages().add(line.split(WHITESPACE)[6]));
    }

    public void addResponseStatusCodes() {

        for (Map.Entry<Integer, String> entry : this.indexedData.entrySet()) {

            String[] parts = entry.getValue().split(WHITESPACE);

            int responseStatusCodeIndex = parts.length - 2;

            if (parts.length > 0 && parts[responseStatusCodeIndex] != null && parts[responseStatusCodeIndex].length() == 3) {
                statistic.getResponseStatusCodes().add(parts[responseStatusCodeIndex]);
            }
        }
    }

    public void addHosts() {
        this.indexedData
                .values()
                .forEach(line -> {
                    statistic.getHostNames().add(line.split(WHITESPACE)[0]);
                });
    }

    public void setResponsesStatusPercentage() {

        int successful = 0;
        int unsuccessful = 0;

        for (String responseCode : statistic.getResponseStatusCodes()) {
            if (responseCode.startsWith("2") || responseCode.startsWith("3"))
                ++successful;
            else
                ++unsuccessful;
        }

        int total = statistic.getResponseStatusCodes().size();

        double successfulPercentage = calculatePercentage(successful, total);
        double unsuccessfulPercentage = calculatePercentage(unsuccessful, total);

        statistic.setSuccessfulRequestsPercentage(successfulPercentage);
        statistic.setUnsuccessfulRequestsPercentage(unsuccessfulPercentage);
    }

    public double getSuccessfulRequestsPercentage() {
        return statistic.getSuccessfulRequestsPercentage();
    }

    public double getUnsuccessfulRequestsPercentage() {
        return statistic.getUnsuccessfulRequestsPercentage();
    }

    public Map<String, Integer> topTenWebpages() {
        Map<String, Integer> map = this.mapWithFrequencies(statistic.getWebpages());
        return topTenMap(map);
    }

    public Map<String, Integer> topTenUnsuccessfulRequests() {

        List<String> list = new ArrayList<>();

        list.addAll(
                statistic.getResponseStatusCodes()
                        .stream()
                        .filter(e -> !e.startsWith("2") && !e.startsWith("3"))
                        .collect(Collectors.toList()));

        Map<String, Integer> map = this.mapWithFrequencies(list);

        return topTenMap(map);
    }

    public Map<String, Integer> topTenHosts() {
        Map<String, Integer> map = this.mapWithFrequencies(statistic.getHostNames());
        return topTenMap(map);
    }

    public void addMalformedLines(Integer splitParts) {

        this.indexedData.entrySet().stream()
                .forEach(line -> {
                    String[] parts = line.getValue().split(WHITESPACE);
                    if (parts.length < splitParts)
                        statistic.getMalformedLinesNumbers().add(line.getKey());
                });
    }

    public List<Integer> getMalformedLinesNumbers() {
        return statistic.getMalformedLinesNumbers();
    }

    private Map<String, Integer> mapWithFrequencies(List<String> list) {

        if (list == null || list.isEmpty())
            throw new IllegalArgumentException("No data in list");

        Map<String, Integer> map = new HashMap<>();

        for (String str : list) {
            Integer frequency = map.get(str);
            map.put(str, (frequency == null) ? 1 : frequency + 1);
        }

        return map;
    }

    private Map<String, Integer> topTenMap(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect((Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)));
    }

    private double calculatePercentage(double a, double b) {
        return a * 100 / b;
    }

}
