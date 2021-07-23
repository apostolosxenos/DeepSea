package com.xenos.deepsea;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service
public class GZipService {

    public Map<Long, String> decompressToMap(Path source) {

        Map<Long, String> indexedData = new LinkedHashMap<>();

        try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(source.toFile()));
             BufferedReader in = new BufferedReader(new InputStreamReader(gis, "UTF-8"))) {

            String line;
            Long lineNumber = 0L;
            while ((line = in.readLine()) != null) {
                indexedData.put(++lineNumber, line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return indexedData;
    }
}