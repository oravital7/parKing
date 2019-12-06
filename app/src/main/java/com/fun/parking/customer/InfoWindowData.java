package com.fun.parking.customer;

public class InfoWindowData {
    private String image;
    private String hotel;
    private String food;
    private String transport;

    public String getImage() {
        return image;
    }

    public InfoWindowData setImage(String image)
    {
        this.image = image;
        return this;
    }

    public String getHotel()
    {
        return hotel;
    }

    public InfoWindowData setHotel(String hotel)
    {
        this.hotel = hotel;
        return this;
    }

    public String getFood()
    {
        return food;
    }

    public InfoWindowData setFood(String food)
    {
        this.food = food;
        return this;
    }

    public String getTransport()
    {
        return transport;
    }

    public InfoWindowData setTransport(String transport)
    {
        this.transport = transport;
        return this;
    }
}
