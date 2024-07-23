package com.college.managerpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.college.managerpengeluaran.R;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;
    private List<modelakun> takeakun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewhistory);
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(transactionAdapter);

        // Inisialisasi DatabaseHelper dan ambil data akun
        DatabaseHelper dbHelper = DatabaseHelper.getDB(this);
        takeakun = dbHelper.AssistAkun().getAkun();

        // Fetch data
        fetchData();


        //navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nb_histori);

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
                return true;
            } else if (itemId == R.id.nb_akun) {
                startActivity(new Intent(getApplicationContext(), ActivityAkun.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }

    private void fetchData() {
        if (takeakun == null || takeakun.isEmpty()) {
            // Menangani kasus ketika akun tidak tersedia
            Log.e("fetchData", "Akun tidak tersedia");
            return;
        }

        String url = "http://192.168.1.13/Expense_Manager/get_data.php?akun_id=" + takeakun.get(0).getAccount_id();
        System.out.println(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response", response.toString());

                            transactionList.clear();

                            double totalIncome = 0.00;
                            double totalExpense = 0.00;

                            JSONArray expenses = response.getJSONArray("expenses");
                            for (int i = 0; i < expenses.length(); i++) {
                                JSONObject expense = expenses.getJSONObject(i);
                                modelekspense transaction = new modelekspense();
                                transaction.setId(expense.getInt("expense_id"));
                                transaction.setTitle(expense.getString("expense_title"));
                                transaction.setQuantity(expense.getString("quantity"));
                                transaction.setPaymentMethod(expense.getString("exp_payment_method"));
                                transaction.setDatetime(expense.getString("datetime"));
                                transaction.setDescription(expense.getString("description"));
                                transaction.setDate(expense.getString("date"));
                                transaction.setAmount(expense.getDouble("expense_amount"));

                                if (!expense.isNull("expense_category_id") && !expense.getString("expense_category_id").isEmpty() && expense.getInt("expense_category_id") != 0) {
                                    transaction.setCategory(expense.getInt("expense_category_id"));
                                } else {
                                    transaction.setCategory(0);
                                }

                                transaction.setUser_id(expense.getInt("user_id"));
                                transaction.setImageDesc(expense.getString("exp_image_desc"));
                                transaction.setIncome(false);
                                transactionList.add(transaction);

                                totalExpense += expense.getDouble("expense_amount");
                            }

                            JSONArray incomes = response.getJSONArray("incomes");
                            for (int i = 0; i < incomes.length(); i++) {
                                JSONObject income = incomes.getJSONObject(i);
                                modelincome transaction = new modelincome();
                                transaction.setId(income.getInt("income_id"));
                                transaction.setTitle(income.getString("income_title"));
                                transaction.setPaymentMethod(income.getString("inc_payment_method"));
                                transaction.setDatetime(income.getString("datetime"));
                                transaction.setDescription(income.getString("description"));
                                transaction.setDate(income.getString("date"));
                                transaction.setAmount(income.getDouble("income_amount"));
                                transaction.setCategory(income.getInt("income_category_id"));
                                transaction.setUser_id(income.getInt("user_id"));
                                transaction.setImageDesc(income.getString("inc_image_desc"));
                                transaction.setIncome(true);
                                transactionList.add(transaction);

                                totalIncome += income.getDouble("income_amount");
                            }

                            Collections.sort(transactionList, new Comparator<Transaction>() {
                                @Override
                                public int compare(Transaction t1, Transaction t2) {
                                    int dateCompare = t2.getDate().compareTo(t1.getDate());
                                    if (dateCompare == 0) {
                                        return Integer.compare(t2.getId(), t1.getId());
                                    }
                                    return dateCompare;
                                }
                            });

                            transactionAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("VolleyError", "Volley error: " + error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(request);
    }
}