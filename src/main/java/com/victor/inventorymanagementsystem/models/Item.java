package com.victor.inventorymanagementsystem.models;

public class Item {
   // private String id;
    private String name;
    private int quantity;
    private double price;
    private String category;

    public Item( String name, int quantity, double price, String category) {
        //this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }
    //public String getId() {return id;}
    public String getName() { return name;}
    public int getQuantity() { return quantity;}
    public double getPrice() { return price;}
    public String getCategory() { return category;}
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        String str = "";
        str +=  getName() + "\n" +
                getQuantity() + "\n" +
                getPrice() + "\n" +
                getCategory() + "\n" + "\n";
        return str;
    }

}
