package com.example.bioshop;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FooldalActivity extends AppCompatActivity {

    private FirebaseUser user;
    private static final String LOG_TAG = FooldalActivity.class.getName();
    private RecyclerView mRecView;
    private ArrayList<ShoppingItem> mItemList;
    private ShoppingItemAdapter mAdapter;
    private int gridNumber=1;
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


        mRecView=findViewById(R.id.recyclerView);
        mRecView.setLayoutManager(new GridLayoutManager(this,gridNumber));
        mItemList=new ArrayList<>();
        mAdapter=new ShoppingItemAdapter(this,mItemList);
        mRecView.setAdapter(mAdapter);
        initializeData();

    }

    private void initializeData() {
        String[] itemList = getResources().getStringArray(R.array.termeknevek);
        String[] iteminfo= getResources().getStringArray(R.array.termekleirasok);
        String[] itemPrice= getResources().getStringArray(R.array.arak);
        TypedArray itemImage= getResources().obtainTypedArray(R.array.termekkepek);
        TypedArray itemsRate= getResources().obtainTypedArray(R.array.ertekelesek);

        mItemList.clear();
        for (int i = 0; i < itemList.length; i++) {
            mItemList.add(new ShoppingItem(itemList[i],iteminfo[i],itemPrice[i],itemsRate.getFloat(i,0),itemImage.getResourceId(i,0)));
        }
        itemImage.recycle();
        mAdapter.notifyDataSetChanged();
    }

}