package com.college.managerpengeluaran;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

import java.math.BigDecimal;
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
    private Context context;
    private List<modelakun> takeakun;

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
        akunAdapter = new AkunAdapter(this, accountList);
        akunAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(akunAdapter);

        buttonTambah.setOnClickListener(v -> TampilTambahAkun());

        // Inisialisasi TextView di onCreate()
        //cobain tambah
        incomeTextView = findViewById(R.id.totalIncome);
        expenseTextView = findViewById(R.id.totalExpense);
        totalbalancedash = findViewById(R.id.totalbalancedash);

        DatabaseHelper dbHelper = DatabaseHelper.getDB(this);
        takeakun = (List<modelakun>) dbHelper.AssistAkun().getAkun();

        if (takeakun.isEmpty()) {
            Toast.makeText(this, "Silahkan pilih atau buat akun terlebih dahulu!", Toast.LENGTH_SHORT).show();
        } else {
            namadash.setText(takeakun.get(0).getAccount_name());
            namadeskripsi.setText(takeakun.get(0).getDescription());
            totalbalancedash.setText("Rp " + takeakun.get(0).getInitial_balance());
            //fetchAkundata();///<< methode tampil list akun\
            totalduit();/// << method tampil total expense (betak dari mainActivity tio)
        }

        //takeakun = inidb.AssistAkun().getAkun();
        //namadash.setText(takeakun.get(0).getAccount_name());
        //namadeskripsi.setText(takeakun.get(0).getDescription());
        //totalbalancedash.setText("Rp " + takeakun.get(0).getInitial_balance());

        fetchAkundata();///<< methode tampil list akun
        //totalduit();/// << method tampil total expense (betak dari mainActivity tio)

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
                response -> {
                    try {
                        accountList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject accountObject = response.getJSONObject(i);
                            int accountId = accountObject.getInt("account_id");
                            String accountName = accountObject.getString("account_name");
                            String description = accountObject.getString("description");
                            BigDecimal initialBalance = BigDecimal.valueOf(accountObject.getInt("initial_balance"));
                            Account account = new Account(accountId, accountName, description, initialBalance);
                            accountList.add(account);
                        }
                        akunAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ActivityAkun.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Fetch Accounts Error", error.toString());
                    Toast.makeText(ActivityAkun.this, "Error fetching accounts", Toast.LENGTH_SHORT).show();
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void totalduit() {
        String url = "http://10.0.2.2:80/Expense_Manager/get_data.php?akun_id=" + takeakun.get(0).getAccount_id();
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
                },
                error -> Log.e("NetworkError", "Error fetching data: " + error.getMessage()));
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

    // click di item recycleview << might delete later ehe
    @Override
    public void onItemClick(int position) {
        Account clickedAccount = accountList.get(position);

        DatabaseHelper inidb = DatabaseHelper.getDB(this);
        int accountId = clickedAccount.getAccountId();
        String accountName = clickedAccount.getAccountName();
        String description = clickedAccount.getDescription();
        BigDecimal initialBalance = clickedAccount.getInitialBalance();
        String date = clickedAccount.getDate();

        modelakun masukini = new modelakun(accountId, accountName, description, String.valueOf(initialBalance), date);

        inidb.AssistAkun().deleteAll();
        inidb.AssistAkun().addto(masukini);

        namadash.setText(clickedAccount.getAccountName());
        namadeskripsi.setText(clickedAccount.getDescription());
        totalbalancedash.setText("Rp " + clickedAccount.getInitialBalance());
    }

    @Override
    public void onEditClick(int position) {
        Account account = accountList.get(position);
        showEditDialog(account);
    }

    @Override
    public void onDeleteClick(int position) {
        Account account = accountList.get(position);
        showDeleteConfirmation(account);
    }

    private void showEditDialog(Account account) {
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(this);
        View editView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_akun, null, false);
        final EditText inputName = editView.findViewById(R.id.input_account_name);
        final EditText inputDescription = editView.findViewById(R.id.input_description);
        final EditText inputBalance = editView.findViewById(R.id.input_initial_balance);
        Button saveButton = editView.findViewById(R.id.button_save);

        inputName.setText(account.getAccountName());
        inputDescription.setText(account.getDescription());
        inputBalance.setText(String.valueOf(account.getInitialBalance()));

        editDialogBuilder.setView(editView);
        AlertDialog editDialog = editDialogBuilder.create();
        editDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        saveButton.setOnClickListener(v -> {
            String newName = inputName.getText().toString();
            String newDescription = inputDescription.getText().toString();
            double newBalance = Double.parseDouble(inputBalance.getText().toString());
            updateAccount(account.getAccountId(), newName, newDescription, newBalance);
            editDialog.dismiss();
        });

        editDialog.show();
    }

    private void showDeleteConfirmation(Account account) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menghapus akun ini?")
                .setPositiveButton("Hapus", (dialog, which) -> deleteAccount(account.getAccountId()))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void updateAccount(int accountId, String name, String description, double balance) {
        String url = "http://10.0.2.2:80/Expense_Manager/update_account.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(ActivityAkun.this, "Akun berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    fetchAkundata(); // Refresh List after update
                },
                error -> {
                    Log.e("Error Update Akun", error.toString());
                    Toast.makeText(ActivityAkun.this, "Error memperbarui akun", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", String.valueOf(accountId));
                params.put("account_name", name);
                params.put("description", description);
                params.put("initial_balance", String.valueOf(balance));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    private void deleteAccount(int accountId) {
        String url = "http://10.0.2.2:80/Expense_Manager/delete_account.php?account_id=" + accountId;

        StringRequest deleteRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("sukses")) {
                        Toast.makeText(ActivityAkun.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        fetchAkundata(); // Refresh data
                    } else {
                        Toast.makeText(ActivityAkun.this, "Delete failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Error Delete Account", error.toString());
                    Toast.makeText(ActivityAkun.this, "Error deleting account", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(deleteRequest);
    }

    //cobain tambahhh pakai alert dialog
    private void TampilTambahAkun() {
        AlertDialog.Builder tambahakunBuilder = new AlertDialog.Builder(this);
        View tambahakuninflate = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_akun, null, false);
        final EditText inputName = tambahakuninflate.findViewById(R.id.input_account_name);
        final EditText inputDescription = tambahakuninflate.findViewById(R.id.input_description);
        final EditText inputBalance = tambahakuninflate.findViewById(R.id.input_initial_balance);
        Button saveButton = tambahakuninflate.findViewById(R.id.button_add_account);

        tambahakunBuilder.setView(tambahakuninflate);
        AlertDialog tambahakun = tambahakunBuilder.create();
        tambahakun.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        saveButton.setOnClickListener(v -> {
            String accountName = inputName.getText().toString();
            String description = inputDescription.getText().toString();
            double initialBalance = Double.parseDouble(inputBalance.getText().toString());
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            TambahAkunKedb(accountName, description, initialBalance, currentDate);
            tambahakun.dismiss();
        });

        tambahakun.show();
    }

    private void TambahAkunKedb(final String accountName, final String description, final double initialBalance, final String date) {
        String url = "http://10.0.2.2:80/Expense_Manager/add_account.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(ActivityAkun.this, "Akun Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                    fetchAkundata(); // Refresh List after addition
                },
                error -> {
                    Log.e("Error Tambah Akun", error.toString());
                    Toast.makeText(ActivityAkun.this, "Error Menambah Akun", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
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
