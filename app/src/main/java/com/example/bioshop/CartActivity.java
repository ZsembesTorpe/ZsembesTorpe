package com.example.bioshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShoppingItemAdapter adapter;
    private ArrayList<ShoppingItem> cartItems;

    private BroadcastReceiver themeChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_cart);

        recyclerView = findViewById(R.id.kosar_vasarlas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = (ArrayList<ShoppingItem>) CartAdapter.getInstance().getCartItems();

        adapter = new ShoppingItemAdapter(this, cartItems);
        recyclerView.setAdapter(adapter);

        TextView totalPriceText = findViewById(R.id.teljes_osszeg);
        float totalPrice = CartAdapter.getInstance().getOsszeg();
        totalPriceText.setText("Összesen: " + String.format("%.0f Ft", totalPrice));



        Button buyButton = findViewById(R.id.buy_from_cart_button);
        buyButton.setOnClickListener(view -> {
            Toast.makeText(this, "Rendelés sikeresen leadva!", Toast.LENGTH_SHORT).show();
            new NotificationHandler(this).send("Köszönjük a vásárlást!");
            CartAdapter.getInstance().clearCart();
            adapter.notifyDataSetChanged();

            totalPriceText.setText("Összesen: 0 Ft");

            Intent intent = new Intent(CartActivity.this, ShopListActivity.class);
            intent.putExtra("clear_cart", true);
            startActivity(intent);


            finish();
        });

        Button backButton = findViewById(R.id.back_from_cart_button);
        backButton.setOnClickListener(view -> {finish();});


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(themeChangeReceiver);
    }
}
