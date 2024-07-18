package com.college.managerpengeluaran;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityAkun extends AppCompatActivity {

    private TextView accountNameTextView, accountDescriptionTextView, totalBalanceTextView,
            incomeTextView, expenseTextView;
    private static final String BASE_URL = "http://10.0.2.2:80/Expense_Manager/";
    private static final String GET_DATA_ENDPOINT = "get_data.php";
    private static final String ACCOUNT_ID_PARAM = "akun_id";
    private RequestQueue requestQueue; // Declare RequestQueue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_akun);

        // Initialize RequestQueue (Good practice to do this in onCreate)
        requestQueue = Volley.newRequestQueue(this);

        // Improve readability by using more descriptive variable names
        accountNameTextView = findViewById(R.id.namadash);
        accountDescriptionTextView = findViewById(R.id.namadeskripsi);
        totalBalanceTextView = findViewById(R.id.totalbalancedash);
        incomeTextView = findViewById(R.id.incomedash);
        expenseTextView = findViewById(R.id.expensedash);

        fetchData();

        // Navbar setup
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
            } else if (itemId == R.id.nb_akun) {
                return true;
            }
            return false;
        });

        //fuck switch case
        /*bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nb_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
                case R.id.nb_tambah:
                    startActivity(new Intent(getApplicationContext(), ActivityTambah.class));
                    break;
                case R.id.nb_histori:
                    startActivity(new Intent(getApplicationContext(), ActivityHistory.class));
                    break;
                case R.id.nb_akun:
                    return true; // Already on this screen
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish(); // Finish current activity to prevent going back with back button
            return true;
        }); */

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchData() {
        String url = BASE_URL + GET_DATA_ENDPOINT + "?" + ACCOUNT_ID_PARAM + "=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("Response", response.toString());

                        // Extract account information
                        JSONObject account = response.getJSONObject("account");
                        String accountName = account.getString("account_name");
                        String description = account.getString("description");
                        double initialBalance = account.getDouble("initial_balance");

                        accountNameTextView.setText(accountName);
                        accountDescriptionTextView.setText(description);
                        totalBalanceTextView.setText(String.format("Rp. %.2f", initialBalance));

                        // Calculate total income and expense
                        double totalIncome = calculateTotal("incomes", "income_amount", response);
                        double totalExpense = calculateTotal("expenses", "expense_amount", response);incomeTextView.setText(String.format("Rp. %.2f", totalIncome));
                        expenseTextView.setText(String.format("Rp. %.2f", totalExpense));

                    } catch (JSONException e) {
                        Log.e("JSONError", "Failed to parse JSON response: " + e.getMessage());
                        // Handle the error appropriately, e.g., display an error message to the user
                    }
                },
                error -> {
                    Log.e("NetworkError", "Error fetching data: " + error.getMessage());
                    // Handle network or server errors, e.g., display an error message
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    // Helper method to calculate total income or expense
    private double calculateTotal(String arrayName, String amountKey, JSONObject response) throws JSONException {
        double total = 0.00;
        JSONArray items = response.getJSONArray(arrayName);
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            total += item.getDouble(amountKey);
        }
        return total;
    }
}