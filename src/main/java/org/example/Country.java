package org.example;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Country {

    private String country;
    private String subregion;
    private String region;
    private int internetUsers;
    private int population;

    public Country(String[] data) {
        this.country = data[0];
        this.subregion = data[1];
        this.region = data[2];
        this.internetUsers = Integer.parseInt(data[3]);
        this.population = Integer.parseInt(data[4]);
    }

    public String getCountry() {
        return country;
    }


    public String getSubregion() {
        return subregion;
    }


    public String getRegion() {
        return region;
    }


    public int getInternetUsers() {
        return internetUsers;
    }

    public int getPopulation() {
        return population;
    }


}

class Database {

    private static Connection connection;
    private static Statement statement;
    private static Map<String, Double> internetUsers;

    public static void initializeCountriesData(ArrayList<Country> countries) throws SQLException {

        establishDatabaseConnection();
        createCountriesTable();

        for (Country currentCountry : countries) {
            insertCountryData(currentCountry);
        }

        fetchInternetUsersBySubregions();
        EventQueue.invokeLater(() -> {
            Graphics graphics = new Graphics(internetUsers);
            graphics.setVisible(true);
        });

        System.out.println("Страна с наименьшим кол-вом интернет пользователей в Восточной Европе:\n" + smallestNumberOfInternetUsersInEasternEurope() + "\n" + "\n");
        System.out.println("Страны, в которых процент интернет пользователей находится в промежутке от 75% до 85%:\n" + internetUsersPercentInRange75And85());

        closeDatabaseConnection();
    }

    private static void establishDatabaseConnection() {
        String newDatabaseUrl  = "jdbc:sqlite:src/main/resources/country.db";

        try {
            connection = DriverManager.getConnection(newDatabaseUrl );
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to connect to the database.", e);
        }
    }


    private static void closeDatabaseConnection() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void handleSQLException(SQLException e) {
        // Обработка исключения SQLException по вашему усмотрению
        e.printStackTrace();
    }


    private static void createCountriesTable() {
        try {
            dropTableIfExists("Countries");

            String createTableQuery = "CREATE TABLE Countries (" +
                    "country VARCHAR, " +
                    "subregion VARCHAR, " +
                    "region VARCHAR, " +
                    "internetUsers INTEGER, " +
                    "population INTEGER);";

            statement.execute(createTableQuery);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void dropTableIfExists(String tableName) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS " + tableName);
    }



    private static final String INSERT_COUNTRY_QUERY = "INSERT INTO Countries (country, subregion, region, internetUsers, population) VALUES (?, ?, ?, ?, ?)";

    private static void insertCountryData(Country country) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COUNTRY_QUERY)) {
            setPreparedStatementValues(preparedStatement, country);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void setPreparedStatementValues(PreparedStatement preparedStatement, Country country) throws SQLException {
        preparedStatement.setString(1, country.getCountry());
        preparedStatement.setString(2, country.getSubregion());
        preparedStatement.setString(3, country.getRegion());
        preparedStatement.setInt(4, country.getInternetUsers());
        preparedStatement.setInt(5, country.getPopulation());
    }

    private static void fetchInternetUsersBySubregions() throws SQLException {
        internetUsers = new HashMap<>();
        String sql =
                "SELECT subregion, SUM(internetUsers) AS totalInternetUsers, SUM(population) AS totalPopulation " +
                        "FROM Countries " +
                        "GROUP BY subregion;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String subregion = resultSet.getString("subregion");
                double totalInternetUsers = resultSet.getDouble("totalInternetUsers");
                double totalPopulation = resultSet.getDouble("totalPopulation");

                double internetUsersPercentage = (totalInternetUsers / totalPopulation) * 100;
                internetUsers.put(subregion, internetUsersPercentage);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }


    private static String smallestNumberOfInternetUsersInEasternEurope() throws SQLException {
        String countryWithSmallestInternetUsers = null;

        String sql =
                "SELECT country, min(internetUsers) AS smallestInternetUsers " +
                        "FROM Countries " +
                        "WHERE subregion = 'Eastern Europe';";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                countryWithSmallestInternetUsers = resultSet.getString("country");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return countryWithSmallestInternetUsers;
    }


    private static java.util.List<String> internetUsersPercentInRange75And85() throws SQLException {
        String sql =
                "SELECT country " +
                        "FROM Countries " +
                        "WHERE (internetUsers * 1.0) / (population * 1.0) * 100 BETWEEN 75 and 85;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            return mapResultSetToCountryList(resultSet);
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return Collections.emptyList();
    }

    private static java.util.List<String> mapResultSetToCountryList(ResultSet resultSet) throws SQLException {
        List<String> countryList = new ArrayList<>();

        while (resultSet.next()) {
            countryList.add(resultSet.getString("country"));
        }

        return countryList;
    }

}