package com.example.bioshop;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter {
    private static CartAdapter instance;
    private final List<ShoppingItem> cartItems;

    private CartAdapter() {
        cartItems = new ArrayList<>();
    }

    public static CartAdapter getInstance() {
        if (instance == null) {
            instance = new CartAdapter();
        }
        return instance;
    }

    public void addItem(ShoppingItem item) {
        for (ShoppingItem cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setCartedCount(cartItem.getCartedCount() + 1);
                return;
            }
        }

        ShoppingItem newItem = new ShoppingItem(
                item.getName(),
                item.getInfo(),
                item.getPrice(),
                item.getRateinfo(),
                item.getImageResource(),
                1
        );

        cartItems.add(newItem);
    }

    public List<ShoppingItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public float getOsszeg() {
        float total = 0;
        for (ShoppingItem item : cartItems) {
            try {
                float price = Float.parseFloat(item.getPrice().replace("Ft", "").replace(",", ".").trim());
                total += price * item.getCartedCount();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return total;
    }
}
