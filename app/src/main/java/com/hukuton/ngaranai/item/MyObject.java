package com.hukuton.ngaranai.item;

public class MyObject {

    private String name;
    private int imageId;

    public MyObject(String name, int imageId){
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
