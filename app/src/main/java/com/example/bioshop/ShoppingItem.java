package com.example.bioshop;

public class ShoppingItem {
    private String name;
    private String info;
    private String price;
    private float rateinfo;

    private  int imageResource;


    public ShoppingItem(String name, String info, String price, float rateinfo, int imageResource) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.rateinfo = rateinfo;
        this.imageResource = imageResource;
    }

    public ShoppingItem() {
    }

    public String getName() {return name;}

    public String getInfo() {return info;}

    public String getPrice() {return price;}

    public float getRateinfo() {return rateinfo;}

    public int getImageResource() {return imageResource;}




}
