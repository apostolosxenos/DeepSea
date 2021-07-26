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
        this.setFrequencies(statistic.getWebpages());
        return topTenMap(statistic.getDataWithFrequencies());
    }

    public Map<String, Integer> topTenUnsuccessfulRequests() {

        List<String> list = new ArrayList<>();

        list.addAll(
                statistic.getResponseStatusCodes()
                        .stream()
                        .filter(e -> !e.startsWith("2") && !e.startsWith("3"))
                        .collect(Collectors.toList()));

        this.setFrequencies(list);

        return topTenMap(statistic.getDataWithFrequencies());
    }

    public Map<String, Integer> topTenHosts() {
        this.setFrequencies(statistic.getHostNames());
        return topTenMap(statistic.getDataWithFrequencies());
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

    private void initDataWithFrequencies() {
        if (!statistic.getDataWithFrequencies().isEmpty())
            statistic.getDataWithFrequencies().clear();
    }

    private void setFrequencies(List<String> list) {

        if (list == null || list.isEmpty())
            throw new IllegalArgumentException("No data in list");

        this.initDataWithFrequencies();

        for (String str : list) {
            Integer frequency = statistic.getDataWithFrequencies().get(str);
            statistic.getDataWithFrequencies().put(str, (frequency == null) ? 1 : frequency + 1);
        }
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
