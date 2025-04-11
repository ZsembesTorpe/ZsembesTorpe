package com.example.bioshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY=MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY=99;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });Log.i(LOG_TAG,"onCreate");
      //  Bundle bundle=getIntent().getExtras();

     // int secret_key=  bundle.getInt("SECRET_KEY");

        int secret_key=getIntent().getIntExtra("SECRET_KEY",0);
        if (secret_key!=99){
            finish();
        }
        emailET = findViewById(R.id.registerED);
        passwordET = findViewById(R.id.RegisterPwd);
        passwordAgainET = findViewById(R.id.RegisterPwdAgain);

        preferences=getSharedPreferences(PREF_KEY,MODE_PRIVATE);

        String email = preferences.getString("email","");
        String password = preferences.getString("password","");
        emailET.setText(email);
        passwordET.setText(password);
        passwordAgainET.setText(password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
         emailET = findViewById(R.id.registerED);
         passwordET = findViewById(R.id.RegisterPwd);
         passwordAgainET = findViewById(R.id.RegisterPwdAgain);

        String emailS= emailET.getText().toString();
        String password= passwordET.getText().toString();
        String passwordAgain=passwordAgainET.getText().toString();

if (!password.equals(passwordAgain)){
    Log.e(LOG_TAG,"A jelszavak nem egyeznek!");
}else {
    Log.i(LOG_TAG, "Regisztráció sikeres! \n Regisztált:" + emailS + "jelszó: " + password);
}

mAuth.createUserWithEmailAndPassword(emailS,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            Log.d(LOG_TAG,"Sikeres regisztráció!");
            overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
            //főoldalt csináld meg ide!
            shopping();
        }else{
            Log.w(LOG_TAG,"Regisztráció sikertelen: ");
            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
        }

    }
});
    }

    public void cancel(View view) {
        finish();
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
    }
    private void shopping(/*felhasználói preferenciák alapján lesz ez kitöltve*/){
        Intent intent =new Intent(this, FooldalActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"onRestart");
    }
}