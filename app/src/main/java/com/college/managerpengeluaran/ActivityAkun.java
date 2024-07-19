package com.college.managerpengeluaran;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        buttonTambah = findViewById(R.id.buttonTambah);

        recyclerView = findViewById(R.id.pilihakun);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        accountList = new ArrayList<>();
        akunAdapter = new AkunAdapter(accountList, this);

        recyclerView.setAdapter(akunAdapter);

        // tambah akun coi
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TampilTambahAkun();
            }
        });

        fetchAkundata();///<< methode tampil list akun

        //cobain tambah
        incomeTextView = findViewById(R.id.totalIncome);
        expenseTextView = findViewById(R.id.totalExpense);
        totalduit(); /// << method tampil total expense (betak dari mainActivity tio)

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
    public void onItemClick(int position) { // click di item recycleview << might delete later ehe
        Account clickedAccount = accountList.get(position);
        namadash.setText(clickedAccount.getAccountName());
        namadeskripsi.setText(clickedAccount.getDescription());
        totalbalancedash.setText("Rp " + clickedAccount.getInitialBalance());
    }

    //cobain tambahhh pakai alert dialog
    private void TampilTambahAkun() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Akun");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_akun, null, false);
        final EditText inputName = viewInflated.findViewById(R.id.input_account_name);
        final EditText inputDescription = viewInflated.findViewById(R.id.input_description);
        final EditText inputBalance = viewInflated.findViewById(R.id.input_initial_balance);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String accountName = inputName.getText().toString();
                String description = inputDescription.getText().toString();
                double initialBalance = Double.parseDouble(inputBalance.getText().toString());
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                TambahAkunKedb(accountName, description, initialBalance, currentDate);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void TambahAkunKedb(final String accountName, final String description, final double initialBalance, final String date) {
        String url = "http://10.0.2.2:80/Expense_Manager/add_account.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ActivityAkun.this, "Akun Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                        fetchAkundata(); // Refresh List nya setelah tambah <<<
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Tambah Akun", error.toString());
                        Toast.makeText(ActivityAkun.this, "Error Menambah Akun", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() { //https://www.w3schools.com/java/java_hashmap.asp
                Map<String, String> params = new HashMap<>();
                params.put("account_name", accountName);
                params.put("description", description);
                params.put("initial_balance", String.valueOf(initialBalance));
                params.put("date", date);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }
}
