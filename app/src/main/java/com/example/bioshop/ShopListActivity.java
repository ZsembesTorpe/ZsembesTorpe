package com.example.bioshop;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ShopListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseUser user;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<ShoppingItem> mItemsData;
    private ShoppingItemAdapter mAdapter;

    private FirebaseFirestore mFireStore;
    private CollectionReference mItems;
private NotificationHandler mNotHand;
    private SharedPreferences preferences;

    private static int queryLimit=8;
    private boolean viewRow = true;
    private AlarmManager mAlaMana;
    private JobScheduler mJbs;
    private BroadcastReceiver themeChanger = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TemaSeged.temaValaszt(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }


        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        mItemsData = new ArrayList<>();

        mAdapter = new ShoppingItemAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
       mFireStore=FirebaseFirestore.getInstance();
       mItems=mFireStore.collection("Items");
        initializeData(); //queryData();
        IntentFilter filter=new IntentFilter();

        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(pwR,filter);


     //   mNotHand=new NotificationHandler(this);
    //    mAlaMana=(AlarmManager) getSystemService(ALARM_SERVICE);
      //  mJbs=(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

     //  setAlarmManager();
       // setJobScheduler();

     //   registerReceiver(themeChanger,new IntentFilter("com.example.bioshop.TEMA_VALTOTT"));
    }
    BroadcastReceiver pwR=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action == null){
                return;
            }
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit=8;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit=4;
                    break;
            }
            initializeData(); //ide a queryData(); jönne,csak valamiért felforgatja az android studio
        }
    };

    //valamiért a queryData() 6600+ adatot feltölt a FireStore-ra és nem tudom, hogy miért


/*private void queryData(){
    mItemsData.clear();

   mItems.orderBy("cartedCount", Query.Direction.ASCENDING).limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                ShoppingItem item=document.toObject(ShoppingItem.class);
                item.setId(document.getId());
                mItemsData.add(item);
            }
if (mItemsData.size()==0){
            initializeData();
            queryData();
        }
        });

        mAdapter.notifyDataSetChanged();



}*/


    private void initializeData() {

        mItemsData.clear();
        String[] itemsList = getResources()
                .getStringArray(R.array.termeknevek);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.termekleirasok);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.arak);
        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.termekkepek);
        TypedArray itemRate = getResources().obtainTypedArray(R.array.ertekelesek);



            for (int i = 0; i < itemsList.length; i++) {
                mItems.add(new ShoppingItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemRate.getFloat(i, 0),
                        itemsImageResources.getResourceId(i, 0),0));
                mItemsData.add(new ShoppingItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemRate.getFloat(i, 0),
                        itemsImageResources.getResourceId(i, 0),0));
            }


        itemsImageResources.recycle();


        mAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
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
        switch (item.getItemId()) {
            case R.id.logout:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.settings:
                Log.d(LOG_TAG, "Setting clicked!");
                Intent intent_beallitasok=new Intent(this,TemaBeallitasok.class);
               startActivity(intent_beallitasok);
                return true;
            case R.id.cart:
                Log.d(LOG_TAG, "Cart clicked!");
                Intent intent_cart=new Intent(this,CartActivity.class);
                startActivity(intent_cart);
                return true;
            case R.id.view_selector:
                if (viewRow) {
                    changeSpanCount(item, R.drawable.baseline_grid_view_24, 1);
                } else {
                    changeSpanCount(item, R.drawable.grid_row, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(/*ShoppingItem item*/) {
        cartItems = (cartItems + 1);
        if (0 < cartItems) {
            countTextView.setText(String.valueOf(cartItems));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);

     /*   mItems.document(item._getId()).update("cartedCount",item.getCartedCount()+1)
                .addOnFailureListener(failure->{
                    Toast.makeText(this,"A terméket "+item._getId()+"nem lehet kosárba tenni",Toast.LENGTH_LONG).show();
                });
                mNotHand.send(item.getname());
        //queryData();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pwR);
        unregisterReceiver(themeChanger);
    }
    public void deleteItem(/*ShoppingItem item*/){
      /*  DocumentReference reference=mItems.document(item._getId());
        reference.delete().addOnSuccessListener(success->{
            Log.d(LOG_TAG,"A termék sikeresen törölve: "+item._getId());
        })
                .addOnFailureListener(failure->{
                    Toast.makeText(this,"A terméket "+item._getId()+"nem lehet törölni",Toast.LENGTH_LONG).show();
                });
           mNotHand.cancel();
        queryData();*/

        cartItems =(cartItems-1);
        if (0 < cartItems) {
            countTextView.setText(String.valueOf(cartItems));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);

    }
    private void updateItem(ShoppingItem item){ }
/*private void setAlarmManager(){
        long idokoz=AlarmManager.INTERVAL_HOUR;
        long tt= SystemClock.elapsedRealtime()+idokoz;
Intent intent = new Intent(this, AlarmReceiver.class);
PendingIntent pi=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
mAlaMana.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,tt,idokoz,pi);
       // mAlaMana.cancel(pi);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setJobScheduler(){
    int nT= JobInfo.NETWORK_TYPE_UNMETERED;
    int hardDL=60*60*1000;
        ComponentName name=new ComponentName(getPackageName(),NotificationJobService.class.getName());
        JobInfo.Builder bobketto=new JobInfo.Builder(0,name)
        .setRequiredNetworkType(nT)
                .setRequiresCharging(true)
                .setOverrideDeadline(hardDL);
        mJbs.schedule(bobketto.build());
        //mJbs.cancel(0);
    }*/
}
