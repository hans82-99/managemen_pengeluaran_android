package com.college.managerpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class crudkategori extends AppCompatActivity {
    Button addtocatdb, balikkeinputpengeluaran;
    EditText valuecategory;
    private List<modelexpcategory> catlist;
    private RecyclerView catlistini;
    private expcatAdapter expcatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudkategori);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addtocatdb = findViewById(R.id.addtocatdb);
        valuecategory = findViewById(R.id.valuecategory);
        balikkeinputpengeluaran = findViewById(R.id.balikkeinputpengeluaran);

        catlist = new ArrayList<>();
        expcatAdapter = new expcatAdapter(catlist, this);
        catlistini = findViewById(R.id.catlistini);
        catlistini.setLayoutManager(new LinearLayoutManager(this));
        catlistini.setAdapter(expcatAdapter);

        balikkeinputpengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent balikcuy = new Intent(crudkategori.this, ActivityTambah.class);
                startActivity(balikcuy);
            }
        });

        new ngambilkategori().execute("http://10.0.2.2:80/Expense_Manager/getcatdb.php");

        addtocatdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = valuecategory.getText().toString();
                simpanKeCatDB(value);
            }
        });

        // navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nb_tambah);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nb_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nb_tambah) {
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

    private void simpanKeCatDB(String value) {
        // Inisialisasi RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // URL endpoint untuk menyimpan data kategori
        String url = "http://10.0.2.2:80/Expense_Manager/simpancatdb.php?expense_category_name=" + value;

        // Membuat StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Menangani respon dari server
                        //valuecategory.setText("");
                        //Toast.makeText(crudkategori.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            modelexpcategory newCategory = new modelexpcategory();
                            newCategory.setId_expense(jsonObject.getInt("expense_category_id"));
                            newCategory.setExpense_category_name(jsonObject.getString("expense_category_name"));
                            catlist.add(newCategory);
                            expcatAdapter.notifyItemInserted(catlist.size() - 1);
                            valuecategory.setText("");
                            Toast.makeText(crudkategori.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(crudkategori.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani error
                        Toast.makeText(crudkategori.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Menambahkan request ke RequestQueue
        requestQueue.add(stringRequest);
    }

    private class ngambilkategori extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();
                response = content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray category = new JSONArray(result);
                for (int i = 0; i < category.length(); i++) {
                    JSONObject jsonObject = category.getJSONObject(i);
                    modelexpcategory masuksini = new modelexpcategory();
                    masuksini.setId_expense(jsonObject.getInt("expense_category_id"));
                    masuksini.setExpense_category_name(jsonObject.getString("expense_category_name"));
                    catlist.add(masuksini);
                }
                expcatAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}