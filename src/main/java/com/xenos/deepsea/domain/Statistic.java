package com.xenos.deepsea.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Statistic {

    private Map<Integer, String> indexedData;
    private Map<String, Integer> dataWithFrequencies;
    private List<String> webpagesAndHosts;
    private List<String> responseStatusCodes;

    private double successfulRequestsPercentage;
    private double unsuccessfulRequestsPercentage;

    public Statistic() {
        this.indexedData = new HashMap<>();
        this.dataWithFrequencies = new HashMap<>();
        this.webpagesAndHosts = new ArrayList<>();
        this.responseStatusCodes = new ArrayList<>();
    }
}
