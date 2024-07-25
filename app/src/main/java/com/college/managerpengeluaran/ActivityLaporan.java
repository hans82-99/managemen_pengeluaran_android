package com.college.managerpengeluaran;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityLaporan extends AppCompatActivity {
    Button buttonyear, buttonmonth, buttonweek, buttontoday, butonrange, butonall;
    TextView incomemasuk, expensemasuk, incomebalance;

    private RecyclerView recyclerView;
    private laporanAdapter laporanAdapter;
    private laporanAdapter laporanAdapterfiltered;
    private List<Transaction> transactionList;
    private List<Transaction> transactionfiltered;
    private List<modelakun> takeakun;
    private NumberFormat currencyFormat;

    public double income = 0.00;
    public double expense = 0.00;
    public double totalIncome = 0.00;
    public double totalExpense = 0.00;
    public double balance = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currencyFormat = NumberFormat.getInstance(new Locale("in", "ID"));

        recyclerView = findViewById(R.id.recyclerViewhistory);
        transactionList = new ArrayList<>();
        transactionfiltered = new ArrayList<>();
        laporanAdapter = new laporanAdapter(transactionList, this);
        laporanAdapterfiltered = new laporanAdapter(transactionfiltered, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(laporanAdapter);

        incomemasuk = findViewById(R.id.incomemasuk);
        expensemasuk = findViewById(R.id.expensemasuk);

        // Inisialisasi DatabaseHelper dan ambil data akun
        DatabaseHelper dbHelper = DatabaseHelper.getDB(this);
        takeakun = dbHelper.AssistAkun().getAkun();

        incomebalance = findViewById(R.id.incomebalance);
        balance = Double.parseDouble(takeakun.get(0).getInitial_balance());
        incomebalance.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(balance)));

        // Fetch data
        fetchData();

        buttontoday = findViewById(R.id.buttonDay);
        buttontoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog inidialog = new Dialog(ActivityLaporan.this);
                inidialog.setContentView(R.layout.filterday);
                inidialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView initanggal = inidialog.findViewById(R.id.inputtglini);
                Button pickdate = inidialog.findViewById(R.id.btnpickdate);
                Button filternow = inidialog.findViewById(R.id.btnfilnow);
                Button batal = inidialog.findViewById(R.id.btnbatal);

                pickdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar kalender = Calendar.getInstance();
                        int tahun = kalender.get(Calendar.YEAR);
                        int bulan = kalender.get(Calendar.MONTH);
                        int hari = kalender.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog ambildate = new DatePickerDialog(
                                ActivityLaporan.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                                        String date = String.format("%d-%02d-%02d", tahun, bulan + 1, hari);
                                        initanggal.setText(date);
                                    }
                                },
                                tahun, bulan, hari
                        );
                        ambildate.show();
                    }
                });

                butonrange = findViewById(R.id.buttonRange);
                butonrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog inidialog = new Dialog(ActivityLaporan.this);
                        inidialog.setContentView(R.layout.filterrange);
                        inidialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView tanggal1 = inidialog.findViewById(R.id.tglmulai);
                        TextView tanggal2 = inidialog.findViewById(R.id.tglakhir);
                        Button startpick = inidialog.findViewById(R.id.btnpickdatestart);
                        Button endpick = inidialog.findViewById(R.id.btnpickdateend);
                        Button batal = inidialog.findViewById(R.id.btnbatal);
                        Button submit = inidialog.findViewById(R.id.btnfilnow);

                        startpick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar kalender = Calendar.getInstance();
                                int tahun = kalender.get(Calendar.YEAR);
                                int bulan = kalender.get(Calendar.MONTH);
                                int hari = kalender.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog ambildate1 = new DatePickerDialog(
                                        ActivityLaporan.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                                                String date = String.format("%d-%02d-%02d", tahun, bulan + 1, hari);
                                                tanggal1.setText(date);
                                            }
                                        },
                                        tahun, bulan, hari
                                );
                                ambildate1.show();
                            }
                        });

                        endpick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar kalender = Calendar.getInstance();
                                int tahun = kalender.get(Calendar.YEAR);
                                int bulan = kalender.get(Calendar.MONTH);
                                int hari = kalender.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog ambildate2 = new DatePickerDialog(
                                        ActivityLaporan.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                                                String date = String.format("%d-%02d-%02d", tahun, bulan + 1, hari);
                                                tanggal2.setText(date);
                                            }
                                        },
                                        tahun, bulan, hari
                                );
                                ambildate2.show();
                            }
                        });
                        batal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inidialog.dismiss();
                            }
                        });

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                filterdaterange(tanggal1.getText().toString(), tanggal2.getText().toString());
                                inidialog.dismiss();
                            }
                        });
                        inidialog.show();
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inidialog.dismiss();
                    }
                });

                filternow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterbydate(initanggal.getText().toString().trim());
                        //System.out.println(initanggal);
                        inidialog.dismiss();
                    }
                });
                inidialog.show();
            }
        });

        butonall = findViewById(R.id.buttonAll);
        butonall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionfiltered.clear();
                incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(totalIncome)));
                expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(totalExpense)));
                laporanAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(laporanAdapter);
            }
        });

        buttonyear = findViewById(R.id.buttonYear);
        buttonyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCurrentYear();
            }
        });

        buttonmonth = findViewById(R.id.buttonMonth);
        buttonmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCurrentMonth();
            }
        });

        //navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nb_laporan);

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
                startActivity(new Intent(getApplicationContext(), ActivityAkun.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nb_laporan) {
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

        String url = "https://mobilekuti2022.web.id/Expense_Manager/get_data.php?akun_id=" + takeakun.get(0).getAccount_id();
        System.out.println(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response", response.toString());

                            transactionList.clear();

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

                            incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(totalIncome)));
                            expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(totalExpense)));

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

                            laporanAdapter.notifyDataSetChanged();

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

    private void filterbydate (String date) {
        transactionfiltered.clear();
        income = 0.00;
        expense = 0.00;

        for (Transaction data : transactionList) {
            if (data.getDate().equals(date)) {
                transactionfiltered.add(data);
                if (data.isIncome()) {
                    income += data.getAmount();
                } else {
                    expense += data.getAmount();
                }
                incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(income)));
                expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(expense)));
                recyclerView.setAdapter(laporanAdapterfiltered);
            }
        }
    }

    private void filterdaterange (String tanggalmulai, String tanggalakhir) {
        SimpleDateFormat formattanggal = new SimpleDateFormat("yyyy-MM-dd");
        transactionfiltered.clear();
        income = 0.00;
        expense = 0.00;

        try {
            Date start = formattanggal.parse(tanggalmulai);
            Date end = formattanggal.parse(tanggalakhir);
            for (Transaction data : transactionList) {
                Date dataDate = formattanggal.parse(data.getDate());
                if (dataDate != null && !dataDate.before(start) && !dataDate.after(end)) {
                    transactionfiltered.add(data);
                    if (data.isIncome()) {
                        income += data.getAmount();
                    } else {
                        expense += data.getAmount();
                    }
                    incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(income)));
                    expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(expense)));
                    recyclerView.setAdapter(laporanAdapterfiltered);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void filterbyname (String name) {
        transactionfiltered.clear();
        income = 0.00;
        expense = 0.00;

        for (Transaction data : transactionList) {
            if (data.getTitle().equals(name)) {
                transactionfiltered.add(data);
                if (data.isIncome()) {
                    income += data.getAmount();
                } else {
                    expense += data.getAmount();
                }
                incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(income)));
                expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(expense)));
                laporanAdapterfiltered.notifyDataSetChanged();
                recyclerView.setAdapter(laporanAdapterfiltered);
            }
        }
    }

    private void filterByCurrentYear() {
        transactionfiltered.clear();
        income = 0.00;
        expense = 0.00;

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (Transaction data : transactionList) {
            String dateString = data.getDate();
            SimpleDateFormat formattanggal = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = formattanggal.parse(dateString);
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                if (year == currentYear) {
                    transactionfiltered.add(data);
                    if (data.isIncome()) {
                        income += data.getAmount();
                    } else {
                        expense += data.getAmount();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(income)));
        expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(expense)));
        laporanAdapterfiltered.notifyDataSetChanged();
        recyclerView.setAdapter(laporanAdapterfiltered);
    }

    private void filterByCurrentMonth() {
        transactionfiltered.clear();
        income = 0.00;
        expense = 0.00;

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        for (Transaction data : transactionList) {
            String dateString = data.getDate();
            SimpleDateFormat formattanggal = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = formattanggal.parse(dateString);
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                if (month == currentMonth && year == currentYear) {
                    transactionfiltered.add(data);
                    if (data.isIncome()) {
                        income += data.getAmount();
                    } else {
                        expense += data.getAmount();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        incomemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(income)));
        expensemasuk.setText("Rp. " + currencyFormat.format(BigDecimal.valueOf(expense)));
        laporanAdapterfiltered.notifyDataSetChanged();
        recyclerView.setAdapter(laporanAdapterfiltered);
    }
}