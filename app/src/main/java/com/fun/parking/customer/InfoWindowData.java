package com.fun.parking.customer;

public class InfoWindowData {
    private String address;
    private String price;

    public String getPrice()
    {
        return price;
    }

    public String getAddress()
    {
        return address;
    }

    public InfoWindowData setAddress(String address)
    {
        this.address = address;
        return this;
    }

    public InfoWindowData setPrice(String price)
    {
        this.price = price;
        return this;
    }
}
