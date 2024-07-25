package com.college.managerpengeluaran;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityPemasukan extends AppCompatActivity {
    EditText namapemasukan, jumlahpemasukan, mediapembayaran, deskripsipemasukan;
    TextView hasiltanggal;
    Button crudkategori, intentpengeluaran, intentpemasukan, tambahtanggal, buatkedashboard, buatstay;
    private ArrayList<modelinccategory> categoryList;
    private ArrayAdapter<modelinccategory> adapter;
    private int selectedCategoryId;
    private List<modelakun> takeakun;
    private Context context;
    Bitmap exp_image_desc;
    ImageView viewgambarpengeluaran;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    //private static final String BASE_URL = "http://192.168.1.13/Expense_Manager/";
    private static final String BASE_URL = "https://mobilekuti2022.web.id/Expense_Manager/";
    //private static final String BASE_URL = "http://10.0.2.2:80/Expense_Manager/";
    /*private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasukan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryList = new ArrayList<>();

        crudkategori = findViewById(R.id.crudkategori);
        intentpengeluaran = findViewById(R.id.intentpengeluaran);
        intentpemasukan = findViewById(R.id.intentpemasukan);
        namapemasukan = findViewById(R.id.namapemasukan);
        jumlahpemasukan = findViewById(R.id.jumlahpemasukan);

        jumlahpemasukan.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    jumlahpemasukan.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp.,]", "");

                    if (!cleanString.isEmpty()) {
                        try {
                            double parsed = Double.parseDouble(cleanString);
                            String formatted = formatCurrency(cleanString);
                            current = formatted;
                            jumlahpemasukan.setText(formatted);
                            jumlahpemasukan.setSelection(formatted.length());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        current = "";
                        jumlahpemasukan.setText("");
                    }

                    jumlahpemasukan.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mediapembayaran = findViewById(R.id.mediapembayaran);
        tambahtanggal = findViewById(R.id.tambahtanggal);
        hasiltanggal = findViewById(R.id.hasiltanggal);
        buatkedashboard = findViewById(R.id.buatkedashboard);
        buatstay = findViewById(R.id.buatstay);
        deskripsipemasukan = findViewById(R.id.deskripsipemasukan);
        viewgambarpengeluaran = findViewById(R.id.viewgambarpengeluaran);

        viewgambarpengeluaran.setOnClickListener(V -> PickImage());

        RegisterResult();

        LocalDateTime waktu = LocalDateTime.now();
        DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatjam = DateTimeFormatter.ofPattern("HH:mm:ss");
        String tampilwaktu = waktu.format(formatwaktu);
        String tampiljam = waktu.format(formatjam);
        String tanggal = tampilwaktu.toString();

        hasiltanggal.setText(tanggal);
        tambahtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar kalender = Calendar.getInstance();
                int tahun = kalender.get(Calendar.YEAR);
                int bulan = kalender.get(Calendar.MONTH);
                int hari = kalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog ambildate = new DatePickerDialog(
                        ActivityPemasukan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                                String date = String.format("%d-%02d-%02d", tahun, bulan + 1, hari);
                                hasiltanggal.setText(date);
                            }
                        },
                        tahun, bulan, hari
                );
                ambildate.show();
            }
        });

        //Account account = new Account();
        DatabaseHelper dbHelper = DatabaseHelper.getDB(this);
        takeakun = dbHelper.AssistAkun().getAkun();

        crudkategori.setOnClickListener(v -> startActivity
                (new Intent(getApplicationContext(), crudkategoriincome.class))
        );

        intentpemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Anda Sudah berada pada tampilan pengeluaran", Toast.LENGTH_SHORT).show();
            }
        });

        intentpengeluaran.setOnClickListener(v -> startActivity
                (new Intent(getApplicationContext(), ActivityTambah.class))
        );

        fetchCategories();

        buatkedashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = cleanCurrencyFormat(jumlahpemasukan.getText().toString());
                if (namapemasukan.getText().toString().isEmpty() ||
                        jumlahpemasukan.getText().toString().isEmpty() ||
                        mediapembayaran.getText().toString().isEmpty() ||
                        deskripsipemasukan.getText().toString().isEmpty() ||
                        hasiltanggal.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityPemasukan.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    clearSharedPreferences();
                    new inputincome().execute(
                            namapemasukan.getText().toString(),
                            amount,
                            mediapembayaran.getText().toString(),
                            deskripsipemasukan.getText().toString(),
                            hasiltanggal.getText().toString(),
                            exp_image_desc
                            //bitmap // pass bitmap to AsyncTask
                            //String.valueOf(account.getAccountId()),
                            //String.valueOf(selectedCategoryId)
                    );
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        buatstay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = cleanCurrencyFormat(jumlahpemasukan.getText().toString());
                if (namapemasukan.getText().toString().isEmpty() ||
                        jumlahpemasukan.getText().toString().isEmpty() ||
                        mediapembayaran.getText().toString().isEmpty() ||
                        deskripsipemasukan.getText().toString().isEmpty() ||
                        hasiltanggal.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityPemasukan.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    clearSharedPreferences();
                    new inputincome().execute(
                            namapemasukan.getText().toString(),
                            amount,
                            mediapembayaran.getText().toString(),
                            deskripsipemasukan.getText().toString(),
                            hasiltanggal.getText().toString(),
                            exp_image_desc
                            //bitmap // pass bitmap to AsyncTask
                            //String.valueOf(account.getAccountId()),
                            //String.valueOf(selectedCategoryId)
                    );
                    Toast.makeText(ActivityPemasukan.this, "Berhasil menginput data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadDataFromSharedPreferences();

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

    private void RegisterResult() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                    viewgambarpengeluaran.setImageBitmap(bitmap);
                                    exp_image_desc = bitmap; // Simpan bitmap jika perlu
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ActivityPemasukan.this, "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // ngecek return bitmap dari Extras
                                Bundle extras = data.getExtras();
                                if (extras != null) {
                                    Bitmap bitmap = (Bitmap) extras.get("data");
                                    if (bitmap != null) {
                                        viewgambarpengeluaran.setImageBitmap(bitmap);
                                        exp_image_desc = bitmap; // Simpan bitmap jika perlu
                                    } else {
                                        Toast.makeText(ActivityPemasukan.this, "No image selected", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ActivityPemasukan.this, "No image selected", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(ActivityPemasukan.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void PickImage() {
        Intent pickIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        pickIntent.setType("image/*"); // ganti aja ke apa gitu nama kalo mao

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {captureIntent});

        activityResultLauncher.launch(chooserIntent);
    }

    private void fetchCategories() {
        String url = BASE_URL + "getincategory.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("income_category_id");
                        String name = jsonObject.getString("income_category_name");
                        categoryList.add(new modelinccategory(id, name));
                    }
                    setupSpinner();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityPemasukan.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.inikategori);
        adapter = new IncCategoryAdapter(this, categoryList);
        //adapter.setDropDownViewResource(R.layout.spinnerkategori);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelinccategory selectedCategory = (modelinccategory) parent.getItemAtPosition(position);
                selectedCategoryId = selectedCategory.getIncome_category_id();
                //Toast.makeText(ActivityTambah.this, "Selected: " + selectedCategory.getExpense_category_name(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    class inputincome extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            String income_title = (String) params[0];
            String income_amount = (String) params[1];
            String inc_payment_method = (String) params[2];
            String description = (String) params[3];
            String date = (String) params[4];
            Bitmap bitmap = (Bitmap) params[5];
            //String user_id = arg0[6];
            //String expense_category_id = arg0[7];

            // Encode image to Base64 if bitmap is not null
            String inc_image_desc = "NULL";
            if (bitmap != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                inc_image_desc = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
            }
            String user_id = String.valueOf(takeakun.get(0).getAccount_id());
            String income_category_id = String.valueOf(selectedCategoryId);
            //String datetime = arg0[9];

            LocalDateTime waktu = LocalDateTime.now();
            DateTimeFormatter formatjam = DateTimeFormatter.ofPattern("HH:mm:ss");
            String tampiljam = waktu.format(formatjam);

            String hasil = "";
            HttpURLConnection conn = null;

            try {
                //URL url = new URL("http://10.0.2.2:80/Expense_Manager/simpanexpense.php");
                URL url = new URL(BASE_URL + "simpanincome.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = URLEncoder.encode("income_title", "UTF-8") + "=" + URLEncoder.encode(income_title, "UTF-8");
                data += "&" + URLEncoder.encode("income_amount", "UTF-8") + "=" + URLEncoder.encode(income_amount, "UTF-8");
                data += "&" + URLEncoder.encode("inc_payment_method", "UTF-8") + "=" + URLEncoder.encode(inc_payment_method, "UTF-8");
                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("datetime", "UTF-8") + "=" + URLEncoder.encode(tampiljam, "UTF-8");
                data += "&" + URLEncoder.encode("inc_image_desc", "UTF-8") + "=" + URLEncoder.encode(inc_image_desc, "UTF-8");

                // Debug hasil encoding
                String encodedUserId = URLEncoder.encode(user_id, "UTF-8");
                String encodedIncomeCategoryId = URLEncoder.encode(income_category_id, "UTF-8");

                data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + encodedUserId;
                data += "&" + URLEncoder.encode("income_category_id", "UTF-8") + "=" + encodedIncomeCategoryId;

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                hasil = sb.toString();
                return hasil;

            } catch (Exception e) {
                Log.e("ActivityTambah", "Exception: " + e.getMessage(), e);
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("insisActivity", "Result: " + result);

            String amount = cleanCurrencyFormat(jumlahpemasukan.getText().toString());
            Double jumlah = Double.parseDouble(takeakun.get(0).getInitial_balance().toString()) + Double.parseDouble(amount);
            String tampungid = String.valueOf(takeakun.get(0).getAccount_id());
            String tampungname = takeakun.get(0).getAccount_name();
            String tampungdesc = takeakun.get(0).getDescription();
            String tampungdate = hasiltanggal.getText().toString();

            DatabaseHelper dbHelper = DatabaseHelper.getDB(ActivityPemasukan.this);
            dbHelper.AssistAkun().deleteAll();
            modelakun masukini = new modelakun(tampungid, tampungname, tampungdesc, String.valueOf(jumlah), tampungdate);
            dbHelper.AssistAkun().addto(masukini);

            new crudforbalance(ActivityPemasukan.class, this, 1).execute(tampungid, tampungname, tampungdesc, String.valueOf(jumlah), tampungdate);

            /*
            LocalDateTime waktu = LocalDateTime.now();
            DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String tampilwaktu = waktu.format(formatwaktu);
            String tanggal = tampilwaktu.toString();
             */

            namapemasukan.setText("");
            jumlahpemasukan.setText("");
            mediapembayaran.setText("");
            deskripsipemasukan.setText("");
            //hasiltanggal.setText(tanggal);
        }
    }

    private void saveDataToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("inputpemasukan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("namapemasukan", namapemasukan.getText().toString());
        editor.putString("jumlahpemasukan", jumlahpemasukan.getText().toString());
        editor.putString("mediapembayaran", mediapembayaran.getText().toString());
        editor.putString("deskripsipemasukan", deskripsipemasukan.getText().toString());
        editor.putString("hasiltanggal", hasiltanggal.getText().toString());
        editor.apply();
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("inputpemasukan", MODE_PRIVATE);
        namapemasukan.setText(sharedPreferences.getString("namapemasukan", ""));
        jumlahpemasukan.setText(sharedPreferences.getString("jumlahpemasukan", ""));
        mediapembayaran.setText(sharedPreferences.getString("mediapembayaran", ""));
        deskripsipemasukan.setText(sharedPreferences.getString("deskripsipemasukan", ""));
        hasiltanggal.setText(sharedPreferences.getString("hasiltanggal", ""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataToSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveDataToSharedPreferences();
    }

    public static String formatCurrency(String amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(Double.parseDouble(amount));
    }

    public static String cleanCurrencyFormat(String formattedAmount) {
        return formattedAmount.replaceAll("[Rp.,]", "");
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("inputpemasukan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}