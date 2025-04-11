package com.example.bioshop;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
        private static final String LOG_TAG = MainActivity.class.getName();
        private static final int SECRET_KEY=99;
        private static final int SIGN_IN_GOOGLE=896;
private static final String PREF_KEY=MainActivity.class.getPackage().toString();


EditText emailET;
EditText passwordET;
private SharedPreferences preferences;
private FirebaseAuth mAuth;
private GoogleSignInClient GoogleSIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        preferences=getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        mAuth= FirebaseAuth.getInstance();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSIC= GoogleSignIn.getClient(this, gso);
        Log.i(LOG_TAG,"onCreate");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SIGN_IN_GOOGLE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(acc.getIdToken());
            }catch (ApiException e){
                Log.w(LOG_TAG,"Google bejelentkezés sikertelen!",e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Log.i(LOG_TAG,"Bejelentkezés sikeres!");
                    shopping();
                    overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
                }else{
                    Log.w(LOG_TAG,"Bejelentkezés sikertelen: ");
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

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
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("email",emailET.getText().toString());
        editor.putString("password",passwordET.getText().toString());
        editor.apply();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"onRestart");
    }

    public void login(View view) {
        EditText email=findViewById(R.id.editTextEmail);
        EditText pw=findViewById(R.id.editTextPassword);

        String emailS= email.getText().toString();
        String pws= pw.getText().toString();



        mAuth.signInWithEmailAndPassword(emailS,pws).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i(LOG_TAG,"Bejelentkezés sikeres!");
                    shopping();
                    overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
                }else{
                    Log.w(LOG_TAG,"Bejelentkezés sikertelen: ");
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void googleLogin(View view) {
      Intent sintent= GoogleSIC.getSignInIntent();
      startActivityForResult(sintent,SIGN_IN_GOOGLE);
    }



    private void shopping(){
        Intent intent =new Intent(this, FooldalActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
    public void register(View view) {
        Intent intent =new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);


    }


}