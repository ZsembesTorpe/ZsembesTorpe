package com.example.bioshop;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FooldalActivity extends AppCompatActivity {

    private FirebaseUser user;
    private static final String LOG_TAG = FooldalActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fooldal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            Log.i(LOG_TAG,"Bejelentett felhasználó");

        }else{
            Log.i(LOG_TAG,"NEM bejelentett felhasználó");
            finish();
            overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
        }
    }
}