package com.college.managerpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActivityAkun extends AppCompatActivity implements AkunAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private AkunAdapter akunAdapter;
    private List<Account> accountList;
    private TextView namadash, namadeskripsi, totalbalancedash, incomeTextView, expenseTextView;
    private Button buttonTambah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        namadash = findViewById(R.id.namadash);
        namadeskripsi = findViewById(R.id.namadeskripsi);
        totalbalancedash = findViewById(R.id.totalbalancedash);

        recyclerView = findViewById(R.id.pilihakun);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        accountList = new ArrayList<>();
        akunAdapter = new AkunAdapter(accountList, this);

        recyclerView.setAdapter(akunAdapter);

        // tambah akun coi
        buttonTambah = findViewById(R.id.buttonTambah);
        buttonTambah.setOnClickListener(v -> TambahAkun());
        fetchAkundata();

        //cobain tambah
        incomeTextView = findViewById(R.id.totalIncome);
        expenseTextView = findViewById(R.id.totalExpense);
        totalduit();

        //navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nb_akun);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nb_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nb_tambah) {
                startActivity(new Intent(getApplicationContext(), ActivityTambah.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nb_histori) {
                startActivity(new Intent(getApplicationContext(), ActivityHistory.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else return itemId == R.id.nb_akun;
        });

    }

    private void fetchAkundata() {
        String url = "http://10.0.2.2:80/Expense_Manager/fetch_accounts.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            accountList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject accountObject = response.getJSONObject(i);

                                int accountId = accountObject.getInt("account_id");
                                String accountName = accountObject.getString("account_name");
                                String description = accountObject.getString("description");
                                double initialBalance = accountObject.getDouble("initial_balance");

                                Account account = new Account(accountId, accountName, description, initialBalance);
                                accountList.add(account);
                            }

                            akunAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityAkun.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Fetch Accounts Error", error.toString());
                        Toast.makeText(ActivityAkun.this, "Error fetching accounts", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    //to do besok -> make it akun id into variable so i can change the data when i click it in the recyclerview , and somehow connect into MainActivity
    private void totalduit() {
        String url = "http://10.0.2.2:80/Expense_Manager/get_data.php?akun_id=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        double totalIncome = calculateTotal("incomes", "income_amount", response);
                        double totalExpense = calculateTotal("expenses", "expense_amount", response);
                        incomeTextView.setText(String.format("Rp. %.2f", totalIncome));
                        expenseTextView.setText(String.format("Rp. %.2f", totalExpense));

                    } catch (JSONException e) {
                        Log.e("JSONError", "Failed to parse JSON response: " + e.getMessage());
                    }
                }, error -> Log.e("NetworkError", "Error fetching data: " + error.getMessage()));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private double calculateTotal(String arrayName, String amountKey, JSONObject response) throws JSONException {
        double total = 0.00;
        JSONArray items = response.getJSONArray(arrayName);
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            total += item.getDouble(amountKey);
        }
        return total;
    }


    @Override
    public void onItemClick(int position) {
        Account clickedAccount = accountList.get(position);
        namadash.setText(clickedAccount.getAccountName());
        namadeskripsi.setText(clickedAccount.getDescription());
        totalbalancedash.setText("Rp " + clickedAccount.getInitialBalance());
    }

    private void TambahAkun() {
        Intent intent = new Intent(ActivityAkun.this, AddAccountActivity.class);
        startActivity(intent);
    }
}
