package com.fun.parking.customer.orderHistory;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

public class Order {

    private String mCity, mStreet, mHouseNumber;
    private Date mStartDate, mEndDate;
    private double mTotalPrice, mPricePerHour;

    public String getCity() {
        return mCity;
    }

    public Order setCity(String city) {
        this.mCity = city;
        return this;
    }

    public String getStreet() {
        return mStreet;
    }

    public Order setStreet(String street) {
        this.mStreet = street;
        return this;
    }

    public String getHouseNumber() {
        return mHouseNumber;
    }

    public Order setHouseNumber(String houseNumber) {
        this.mHouseNumber = houseNumber;
        return this;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Order setStartDate(Date startDate) {
        this.mStartDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public Order setEndDate(Date endDate) {
        this.mEndDate = endDate;
        return this;
    }

    public double getTotalPrice() {
        return mTotalPrice;
    }

    public Order setTotalPrice(double totalPrice) {
        this.mTotalPrice = totalPrice;
        return this;
    }

    public Order setPricePerHour(double pricePerHour) {
        this.mPricePerHour = pricePerHour;
        return this;
    }

    public double getHours()
    {
        return mTotalPrice / mPricePerHour;
    }

    public String getAddress()
    {
        return mCity + ", " + mStreet + " " + mHouseNumber;
    }

    public static Order buildWithFireStore(QueryDocumentSnapshot document)
    {
        return new Order().setCity(document.getString("Address.City"))
                .setStreet(document.getString("Address.Street"))
                .setHouseNumber(document.getString("Address.HouseNumber"))
                .setTotalPrice(document.getDouble("Price.Total Price"))
                .setEndDate(document.getDate("Rent.End"))
                .setStartDate(document.getDate("Rent.Start"))
                .setPricePerHour(document.getDouble("Price.Price per hour"));
    }

    @Override
    public String toString() {
        return "Order{" +
                "mCity='" + mCity + '\'' +
                ", mStreet='" + mStreet + '\'' +
                ", mHouseNumber='" + mHouseNumber + '\'' +
                ", mStartDate=" + mStartDate +
                ", mEndDate=" + mEndDate +
                ", mTotalPrice=" + mTotalPrice +
                '}';
    }
}
