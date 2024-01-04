package org.example;

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