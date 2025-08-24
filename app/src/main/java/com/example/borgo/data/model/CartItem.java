package com.example.borgo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private MenuItem food;
    private int quantity;

    public CartItem(MenuItem food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public MenuItem getFood() {
        return food;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return food.getPrice() * quantity;
    }


    // --- Parcelable Implementation (similar to Food model) ---
    protected CartItem(Parcel in) {
        food = in.readParcelable(MenuItem.class.getClassLoader());
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(food, flags);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}