package com.example.peekaboo;

public class CuriosityModel {
    private final String title;
    private final String fact;

    public CuriosityModel(String title, String fact) {
        this.title = title;
        this.fact = fact;
    }

    public String getTitle() {
        return title;
    }

    public String getFact() {
        return fact;
    }
}