package com.xenos.deepsea;

import com.xenos.deepsea.controller.CommandLineController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class DeepseaApplication implements CommandLineRunner {

    @Autowired
    CommandLineController commandLineController;

    public static void main(String[] args) {
        SpringApplication.run(DeepseaApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Path path;

        if (args.length == 1) {
            path = getFilePath(args[0]);
            commandLineController.initFromFile(path);
            commandLineController.displayChoices();
            commandLineController.promptUsersChoice();
        }
    }

    private Path getFilePath(String source) {

        if (!Files.exists(Paths.get(source)))
            throw new IllegalArgumentException("File does not exist!");

        return Paths.get(source);
    }

}
