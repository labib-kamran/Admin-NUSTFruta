package com.labibkamran.adminnustfruta.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails implements Parcelable {
    private String userUid;
    private String userName;
    private ArrayList<String> foodNames;
    private ArrayList<String> foodImages;
    private ArrayList<String> foodPrices;
    private ArrayList<Integer> foodQuantities;
    private String address;
    private String totalPrice;
    private String phoneNumber;
    private boolean orderAccepted;
    private boolean paymentReceived;
    private String itemPushKey;
    private long currentTime;

    public OrderDetails() {
        // Default constructor
    }

    // Constructor using fields
    public OrderDetails(String userUid, String userName, ArrayList<String> foodNames, ArrayList<String> foodImages,
                        ArrayList<String> foodPrices, ArrayList<Integer> foodQuantities, String address,
                        String totalPrice, String phoneNumber, boolean orderAccepted, boolean paymentReceived,
                        String itemPushKey, long currentTime) {
        this.userUid = userUid;
        this.userName = userName;
        this.foodNames = foodNames;
        this.foodImages = foodImages;
        this.foodPrices = foodPrices;
        this.foodQuantities = foodQuantities;
        this.address = address;
        this.totalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.orderAccepted = orderAccepted;
        this.paymentReceived = paymentReceived;
        this.itemPushKey = itemPushKey;
        this.currentTime = currentTime;
    }

    protected OrderDetails(Parcel in) {
        userUid = in.readString();
        userName = in.readString();
        foodNames = in.createStringArrayList();
        foodImages = in.createStringArrayList();
        foodPrices = in.createStringArrayList();
        foodQuantities = new ArrayList<>();
        in.readList(foodQuantities, Integer.class.getClassLoader());
        address = in.readString();
        totalPrice = in.readString();
        phoneNumber = in.readString();
        orderAccepted = in.readByte() != 0;
        paymentReceived = in.readByte() != 0;
        itemPushKey = in.readString();
        currentTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUid);
        dest.writeString(userName);
        dest.writeStringList(foodNames);
        dest.writeStringList(foodImages);
        dest.writeStringList(foodPrices);
        dest.writeList(foodQuantities);
        dest.writeString(address);
        dest.writeString(totalPrice);
        dest.writeString(phoneNumber);
        dest.writeByte((byte) (orderAccepted ? 1 : 0));
        dest.writeByte((byte) (paymentReceived ? 1 : 0));
        dest.writeString(itemPushKey);
        dest.writeLong(currentTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<OrderDetails> CREATOR = new Parcelable.Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    // Getters and setters
    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(ArrayList<String> foodNames) {
        this.foodNames = foodNames;
    }

    public ArrayList<String> getFoodImages() {
        return foodImages;
    }

    public void setFoodImages(ArrayList<String> foodImages) {
        this.foodImages = foodImages;
    }

    public ArrayList<String> getFoodPrices() {
        return foodPrices;
    }

    public void setFoodPrices(ArrayList<String> foodPrices) {
        this.foodPrices = foodPrices;
    }

    public ArrayList<Integer> getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(ArrayList<Integer> foodQuantities) {
        this.foodQuantities = foodQuantities;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOrderAccepted() {
        return orderAccepted;
    }

    public void setOrderAccepted(boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public boolean isPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
