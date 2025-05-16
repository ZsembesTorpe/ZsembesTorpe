package com.example.bioshop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private FirebaseUser user;
    private FrameLayout redCircle;
    private TextView countTextView;
    private int gridNumber=1;
    private int cartItems = 0;
    private int queryLimit=8;
    private RecyclerView mRecView;
    private ArrayList<ShoppingItem> mItemsData;
    private ShoppingItemAdapter mAdapter;
    private TextView contentTextView;
    private NotificationHandler mNotH;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    boolean viewRow= true;
    private AlarmManager mAlaMana;


    private boolean shouldUpdateCart = false;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_list);
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
        mItemsData=new ArrayList<>();
        mAdapter=new ShoppingItemAdapter(this,mItemsData);
        mRecView.setAdapter(mAdapter);


        mFirestore=FirebaseFirestore.getInstance();
        mItems=mFirestore.collection("Items");
        initializeData();
       // queryData();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver,filter);

        mNotH=new NotificationHandler(this);
        mAlaMana=(AlarmManager) getSystemService(ALARM_SERVICE);
        setAlarmManager();
    }
    BroadcastReceiver powerReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action==null){
                return;
            }
            switch (action){
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit=8;
                    queryData();
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit=4;
                    queryData();
                    break;
            }

        }
    };
    private void initializeData() {
        String[] itemList = getResources().getStringArray(R.array.termeknevek);
        String[] iteminfo= getResources().getStringArray(R.array.termekleirasok);
        String[] itemPrice= getResources().getStringArray(R.array.arak);
        TypedArray itemImage= getResources().obtainTypedArray(R.array.termekkepek);
        TypedArray itemsRate= getResources().obtainTypedArray(R.array.ertekelesek);

   // mItemList.clear();
        for (int i = 0; i < itemList.length; i++) {
         /*   mItemsData.add(new ShoppingItem(
                    itemList[i],
                    iteminfo[i],
                    itemPrice[i],
                    itemsRate.getFloat(i,0),
                    itemImage.getResourceId(i,0),0));*/
            mItems.add(new ShoppingItem(
                    itemList[i],
                    iteminfo[i],
                    itemPrice[i],
                    itemsRate.getFloat(i,0),
                    itemImage.getResourceId(i,0),0));

        }

        itemImage.recycle();

    }
    private void queryData() {

        mItemsData.clear();


        mItems.orderBy("name", Query.Direction.DESCENDING).limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document:queryDocumentSnapshots){
                ShoppingItem item=document.toObject(ShoppingItem.class);
               item.setId(document.getId());
                mItemsData.add(item);
            }
            queryLimit=10;
            if (mItemsData.size()==0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();
        });
initializeData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        super.onCreateOptionsMenu(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.settings:
                Log.d(LOG_TAG,"beállítások megnyomva");
                return true;
            case R.id.cart:
                Log.d(LOG_TAG,"kosár megnyomva");
                return true;
            case R.id.view_selector:
                Log.d(LOG_TAG,"get logged");
                if (viewRow){
                    changeSpanCount(item,R.drawable.baseline_grid_view_24,1);
                }else {
                    changeSpanCount(item,R.drawable.grid_row,3);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableID, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableID);
        GridLayoutManager layoutManager= (GridLayoutManager) mRecView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem=menu.findItem(R.id.cart);
        FrameLayout rootView=(FrameLayout) alertMenuItem.getActionView();
        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(v -> onOptionsItemSelected(alertMenuItem));

      if (shouldUpdateCart && countTextView != null && redCircle != null) {
            if (cartItems > 0) {
                countTextView.setText(String.valueOf(cartItems));
                redCircle.setVisibility(View.VISIBLE);
            } else {
                countTextView.setText("");
                redCircle.setVisibility(View.GONE);
            }
            shouldUpdateCart = false;
        }

        return super.onPrepareOptionsMenu(menu);
    }
    public void updateAlertIcon(ShoppingItem item) {
        if (item == null) {

            cartItems = 0;
            countTextView.setText("");
            redCircle.setVisibility(View.GONE);
        }

        cartItems = (cartItems + 1);
        if (0<cartItems) {
            countTextView.setText(String.valueOf(cartItems));
        } else {
            countTextView.setText("");
        }
        redCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);
        mItems.document(item._getId()).update("cartedCount", item.getCartedCount() + 1)
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " cannot be changed.", Toast.LENGTH_LONG).show();
                });

    mNotH.send(item.getName());
    queryData();
    }
    public void deleteItem(ShoppingItem item){
        DocumentReference ref=mItems.document(item._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + item._getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });
        queryData();
        mNotH.cancel();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);

    }
    private void setAlarmManager(){
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
      //  mAlaMana.set
    }

}
