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

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(int internetUsers) {
        this.internetUsers = internetUsers;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}