package com.example.bioshop;

public class ShoppingItem {
    private String name;
    private String info;
    private String price;
    private float rateinfo;

    private  int imageResource;
    private String id;
    private int cartedCount;

    public ShoppingItem(String name, String info, String price, float rateinfo, int imageResource,int cartedCount) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.rateinfo = rateinfo;
        this.imageResource = imageResource;
        this.cartedCount=cartedCount;
    }
    public ShoppingItem() {}

    public String getName() {return name;}

    public String getInfo() {return info;}

    public String getPrice() {return price;}

    public float getRateinfo() {return rateinfo;}

    public int getImageResource() {return imageResource;}

    public String _getId() {return id;}

    public void setId(String id) {this.id = id;}

    public int getCartedCount() {return cartedCount;}

    public void setCartedCount(int count) {this.cartedCount = count;}
}
