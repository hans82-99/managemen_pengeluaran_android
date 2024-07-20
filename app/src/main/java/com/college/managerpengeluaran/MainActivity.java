package com.college.managerpengeluaran;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView namadash, totalbalancedash, incomedash, expensedash;
    private RecyclerView historidash;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;
    private List<modelakun> takeakun;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        namadash = findViewById(R.id.namadash);
        totalbalancedash = findViewById(R.id.totalbalancedash);
        incomedash = findViewById(R.id.incomedash);
        expensedash = findViewById(R.id.expensedash);
        historidash = findViewById(R.id.historidash);

        DatabaseHelper dbHelper = DatabaseHelper.getDB(this);
        takeakun = (List<modelakun>) dbHelper.AssistAkun().getAkun();

        if (takeakun.isEmpty()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Akun Tidak Tersedia!");
            alertDialog.setMessage("Silahkan buat atau pilih akun terlebih dahulu pada halaman akun!");
            alertDialog.setPositiveButton("Beralih Akun", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent keakun = new Intent(getApplicationContext(), ActivityAkun.class);
                }
            });
            alertDialog.setNegativeButton("Nanti Saja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            //Toast.makeText(this, "Silahkan pilih atau buat akun terlebih dahulu!", Toast.LENGTH_SHORT).show();
        } else {
            namadash.setText(takeakun.get(0).getAccount_name());
            totalbalancedash.setText("Rp. " + takeakun.get(0).getInitial_balance() + ",00");
            fetchData();
        }

        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList, this);
        historidash.setLayoutManager(new LinearLayoutManager(this));
        historidash.setAdapter(transactionAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nb_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nb_home) {
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
        String url = "http://10.0.2.2:80/Expense_Manager/get_data.php?akun_id=" + takeakun.get(0).getAccount_id();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response", response.toString());

                            /*
                            JSONObject account = response.getJSONObject("account");
                            Account accountData = new Account();
                            if (accountData.isEmpty()) {
                                accountData.setAccountId(account.getInt("account_id"));
                                accountData.setAccountName(account.getString("account_name"));
                                accountData.setInitialBalance(account.getDouble("initial_balance"));
                                accountData.setDescription(account.getString("description"));
                                namadash.setText(account.getString("account_name"));
                                totalbalancedash.setText("Rp. " + account.getDouble("initial_balance"));
                                System.out.println(accountData.getAccountId());
                            }
                             */

                            transactionList.clear(); // Clear existing data before adding new data

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
                                transaction.setCategory(expense.getInt("expense_category_id"));
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

                            // Ini buat income dan expense profile
                            incomedash.setText("Rp. " + totalIncome);
                            expensedash.setText("Rp. " + totalExpense);

                            // Ini buat ngurutin data berdasarkan date terkini
                            Collections.sort(transactionList, new Comparator<Transaction>() {
                                @Override
                                public int compare(Transaction t1, Transaction t2) {
                                    int dateCompare = t2.getDate().compareTo(t1.getDate());
                                    if (dateCompare == 0) {
                                        return Integer.compare(t2.getId(), t1.getId()); // Jika date sama, urutkan berdasarkan id
                                    }
                                    return dateCompare; // Urutkan berdasarkan date
                                }
                            });

                            // Potong daftar transaksi agar hanya berisi 5 item terbaru
                            List<Transaction> limitedTransactionList = transactionList.size() > 5 ? transactionList.subList(0, 5) : transactionList;

                            // Perbarui adapter dengan daftar transaksi yang sudah dipotong
                            transactionAdapter.updateData(limitedTransactionList);
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
