package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDatabase() throws SQLException {
        List<Country> countries = parseCountriesFromFile("src/main/resources/Country.csv");
        Database.initializeCountriesData((ArrayList<Country>) countries);
    }

    public static List<Country> parseCountriesFromFile(String filePath) {
        try {
            return Files.lines(Paths.get(filePath))
                    .skip(1) // Skip the header line
                    .map(line -> line.split(","))
                    .map(Country::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}