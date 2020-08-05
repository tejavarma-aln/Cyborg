package com.example.cyborg.Models;

public class DashBoardCardModel {

    private String name;
    private int imageId;

    public DashBoardCardModel(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
