package com.example.shopapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Array;
import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity {
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private ArrayList<ShoppingItem> itemList;
    private ShoppingItemAdapter adapter;

    private int gridNumber = 1;

    private int cartItems = 0;

    private boolean viewRow = true;

    private FrameLayout redCircle;
    private TextView contextTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {
            finish();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList = new ArrayList<>();

        adapter = new ShoppingItemAdapter(this, itemList);

        recyclerView.setAdapter(adapter);

        initializeData();
    }

    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources().getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources().getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.shopping_item_images);
        TypedArray itemsRate = getResources().obtainTypedArray(R.array.shopping_item_rates);

        this.itemList.clear();

        for (int i = 0; i < itemsList.length; i++) {
            this.itemList.add(new ShoppingItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemsRate.getFloat(i, 0), itemsImageResource.getResourceId(i, 0)));
        }

        itemsImageResource.recycle();

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if (itemId == R.id.log_out_button) {
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else if (itemId == R.id.settings_button) {
            return true;
        } else if (itemId == R.id.cart) {
            return true;
        } else if (itemId == R.id.view_selector) {
            if(viewRow) {
                changeSpanCount(item, R.drawable.ic_view_grid, 1);
            } else {
                changeSpanCount(item, R.drawable.ic_view_row,2);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contextTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return true;
    }

    public void updateAlertIcon() {
        cartItems = (cartItems + 1);
        if (0 < cartItems) {
            contextTextView.setText(String.valueOf(cartItems));
        } else {
            contextTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }
}