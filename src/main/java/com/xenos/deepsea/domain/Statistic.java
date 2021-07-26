package com.xenos.deepsea.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class Statistic {

    private Map<String, Integer> dataWithFrequencies;
    private List<String> webpages;
    private List<String> responseStatusCodes;
    private List<String> hostNames;
    private List<Integer> malformedLinesNumbers;

    private double successfulRequestsPercentage;
    private double unsuccessfulRequestsPercentage;

    public Statistic() {
        this.dataWithFrequencies = new HashMap<>();
        this.webpages = new ArrayList<>();
        this.responseStatusCodes = new ArrayList<>();
        this.hostNames = new ArrayList<>();
        this.malformedLinesNumbers = new ArrayList<>();
    }
}
