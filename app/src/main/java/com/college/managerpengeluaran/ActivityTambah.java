package com.college.managerpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityTambah extends AppCompatActivity {
    Button crudkategori, intentpengeluaran, intentpemasukan;
    private ArrayList<modelexpcategory> categoryList;
    private ArrayAdapter<modelexpcategory> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryList = new ArrayList<>();

        crudkategori = findViewById(R.id.crudkategori);
        intentpengeluaran = findViewById(R.id.intentpengeluaran);
        intentpemasukan = findViewById(R.id.intentpemasukan);

        crudkategori.setOnClickListener(v -> startActivity
                (new Intent(getApplicationContext(), crudkategori.class))
        );

        intentpengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Anda Sudah berada pada tampilan pengeluaran", Toast.LENGTH_SHORT).show();
            }
        });

        intentpemasukan.setOnClickListener(v -> startActivity
                (new Intent(getApplicationContext(), ActivityTambah.class))
        );

        fetchCategories();

        //spinner
        /*
        Spinner spinner = findViewById(R.id.inikategori);
        ArrayList<modelexpcategory> adapter = new ArrayList<>(this, R.array.kategori_array, R.layout.spinnerkategori);
        adapter.setDropDownViewResource(R.layout.spinnerkategori);
        spinner.setAdapter(adapter);
         */

        //navbar
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

    private void fetchCategories() {
        String url = "http://10.0.2.2:80/Expense_Manager/getcatdb.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("expense_category_id");
                        String name = jsonObject.getString("expense_category_name");
                        categoryList.add(new modelexpcategory(id, name));
                    }
                    setupSpinner();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityTambah.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.inikategori);
        adapter = new ExpCategoryAdapter(this, categoryList);
        //adapter.setDropDownViewResource(R.layout.spinnerkategori);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelexpcategory selectedCategory = (modelexpcategory) parent.getItemAtPosition(position);
                //Toast.makeText(ActivityTambah.this, "Selected: " + selectedCategory.getExpense_category_name(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

}