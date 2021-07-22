package com.xenos.deepsea;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

@Service
public class GZipService {

    public String decompressToString(Path source) {

        StringBuilder sb = new StringBuilder();

        try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(source.toFile()));
             BufferedReader in = new BufferedReader(new InputStreamReader(gis, "UTF-8"))) {

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }
}