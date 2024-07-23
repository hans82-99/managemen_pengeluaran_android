package com.college.managerpengeluaran;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.Manifest;


import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityTambah extends AppCompatActivity {
    EditText namapengeluaran, jumlahpengeluaran, mediapembayaran, quantitypengeluaran, deskripsipengeluaran;
    TextView hasiltanggal;
    Button crudkategori, intentpengeluaran, intentpemasukan, tambahtanggal, buatkedashboard, buatstay, buttongambarpengeluaran;
    private ArrayList<modelexpcategory> categoryList;
    private ArrayAdapter<modelexpcategory> adapter;
    private int selectedCategoryId;
    private List<modelakun> takeakun;
    private Context context;
    Bitmap exp_image_desc;
    ImageView viewgambarpengeluaran;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final String BASE_URL = "http://192.168.1.13/Expense_Manager/";
    //private static final String BASE_URL = "http://10.0.2.2:80/Expense_Manager/";
    /*private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };*/


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
        namapengeluaran = findViewById(R.id.namapengeluaran);
        jumlahpengeluaran = findViewById(R.id.jumlahpengeluaran);
        mediapembayaran = findViewById(R.id.mediapembayaran);
        quantitypengeluaran = findViewById(R.id.quantitypengeluaran);
        tambahtanggal = findViewById(R.id.tambahtanggal);
        hasiltanggal = findViewById(R.id.hasiltanggal);
        buatkedashboard = findViewById(R.id.buatkedashboard);
        buatstay = findViewById(R.id.buatstay);
        deskripsipengeluaran = findViewById(R.id.deskripsipengeluaran);
        viewgambarpengeluaran = findViewById(R.id.viewgambarpengeluaran);

        //buttongambarpengeluaran = findViewById(R.id.buttongambarpengeluaran);

        //buat tambah gambar
        //ImageView viewgambarpengeluaran = findViewById(R.id.viewgambarpengeluaran);
        buttongambarpengeluaran = findViewById(R.id.buttongambarpengeluaran);
        RegisterResult();

        buttongambarpengeluaran.setOnClickListener(v -> PickImage());


        // Menyiapkan ActivityResultLauncher
        /*activityResultLauncher = registerForActivityResult(
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
                                    Toast.makeText(ActivityTambah.this, "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ActivityTambah.this, "No image selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );*/

        /*buttongambarpengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pick=true;
                if (pick==true){
                    if(!checkCameraPermission()){
                        requestCameraPermission();

                    }else PickImage();

                }else {
                    if(!checkStoragePermission()){
                        requestStoragePermission();

                    }else PickImage();
                }
            }
        });*/


        // Setup listener untuk tombol gambar
        /*buttongambarpengeluaran.setOnClickListener(v -> new AlertDialog.Builder(ActivityTambah.this)
                .setTitle("Pilih Sumber")
                .setItems(new CharSequence[]{"Ambil Foto", "Pilih dari Galeri"},
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    openCamera(); // Ambil foto dari kamera
                                    break;
                                case 1:
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    activityResultLauncher.launch(intent); // Pilih foto dari galeri
                                    break;
                            }
                        }).show()
        );*/

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
                        ActivityTambah.this,
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

        buatkedashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namapengeluaran.getText().toString().isEmpty() ||
                        jumlahpengeluaran.getText().toString().isEmpty() ||
                        mediapembayaran.getText().toString().isEmpty() ||
                        quantitypengeluaran.getText().toString().isEmpty() ||
                        deskripsipengeluaran.getText().toString().isEmpty() ||
                        hasiltanggal.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityTambah.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    new inputexpense().execute(
                            namapengeluaran.getText().toString(),
                            jumlahpengeluaran.getText().toString(),
                            mediapembayaran.getText().toString(),
                            quantitypengeluaran.getText().toString(),
                            deskripsipengeluaran.getText().toString(),
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
                if (namapengeluaran.getText().toString().isEmpty() ||
                        jumlahpengeluaran.getText().toString().isEmpty() ||
                        mediapembayaran.getText().toString().isEmpty() ||
                        quantitypengeluaran.getText().toString().isEmpty() ||
                        deskripsipengeluaran.getText().toString().isEmpty() ||
                        hasiltanggal.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityTambah.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    new inputexpense().execute(
                            namapengeluaran.getText().toString(),
                            jumlahpengeluaran.getText().toString(),
                            mediapembayaran.getText().toString(),
                            quantitypengeluaran.getText().toString(),
                            deskripsipengeluaran.getText().toString(),
                            hasiltanggal.getText().toString(),
                            exp_image_desc
                            //String.valueOf(account.getAccountId()),
                            //String.valueOf(selectedCategoryId)
                    );
                    Toast.makeText(ActivityTambah.this, "Berhasil menambahkan pengeluaran", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    private void RegisterResult(){
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
                                    Toast.makeText(ActivityTambah.this, "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ActivityTambah.this, "No image selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }
    /*private  void RegisterResult(){
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            Picasso.get().load(imageUri).into(viewgambarpengeluaran);
                        } else {
                            Toast.makeText(ActivityTambah.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                        } catch (Exception e){
                            Toast.makeText(ActivityTambah.this, "Error loading image", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

    }*/

    /*private void PickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(intent);
    }*/
    private void PickImage() {
        Intent pickIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        pickIntent.setType("image/*");

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {captureIntent});

        activityResultLauncher.launch(chooserIntent);
    }


    /*
    private void checkPermissions() {
        // Memeriksa apakah izin sudah diberikan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, mintalah izin
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, Anda bisa melanjutkan dengan memilih gambar
            } else {
                // Izin ditolak, beri tahu pengguna
                Toast.makeText(this, "Permission denied. Unable to access gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            activityResultLauncher.launch(takePictureIntent);
        }
    }*/


    // Spinner
    private void fetchCategories() {
        String url = BASE_URL + "getcatdb.php";
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
                selectedCategoryId = selectedCategory.getId_expense();
                //Toast.makeText(ActivityTambah.this, "Selected: " + selectedCategory.getExpense_category_name(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    class inputexpense extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            String expense_title = (String) params[0];
            String expense_amount = (String) params[1];
            String exp_payment_method = (String) params[2];
            String quantity = (String) params[3];
            String description = (String) params[4];
            String date = (String) params[5];
            Bitmap bitmap = (Bitmap) params[6];
            //String user_id = arg0[6];
            //String expense_category_id = arg0[7];

            // Encode image to Base64 if bitmap is not null
            String exp_image_desc = "NULL";
            if (bitmap != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                exp_image_desc = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
            }
            String user_id = String.valueOf(takeakun.get(0).getAccount_id());
            String expense_category_id = String.valueOf(selectedCategoryId);
            //String datetime = arg0[9];

            LocalDateTime waktu = LocalDateTime.now();
            DateTimeFormatter formatjam = DateTimeFormatter.ofPattern("HH:mm:ss");
            String tampiljam = waktu.format(formatjam);

            String hasil = "";
            HttpURLConnection conn = null;

            try {
                //URL url = new URL("http://10.0.2.2:80/Expense_Manager/simpanexpense.php");
                URL url = new URL(BASE_URL + "simpanexpense.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = URLEncoder.encode("expense_title", "UTF-8") + "=" + URLEncoder.encode(expense_title, "UTF-8");
                data += "&" + URLEncoder.encode("expense_amount", "UTF-8") + "=" + URLEncoder.encode(expense_amount, "UTF-8");
                data += "&" + URLEncoder.encode("exp_payment_method", "UTF-8") + "=" + URLEncoder.encode(exp_payment_method, "UTF-8");
                data += "&" + URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");
                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("datetime", "UTF-8") + "=" + URLEncoder.encode(tampiljam, "UTF-8");
                data += "&" + URLEncoder.encode("exp_image_desc", "UTF-8") + "=" + URLEncoder.encode(exp_image_desc, "UTF-8");
                data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                data += "&" + URLEncoder.encode("expense_category_id", "UTF-8") + "=" + URLEncoder.encode(expense_category_id, "UTF-8");

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
            Double jumlah = Double.parseDouble(takeakun.get(0).getInitial_balance().toString()) - Double.parseDouble(jumlahpengeluaran.getText().toString());
            String tampungid = String.valueOf(takeakun.get(0).getAccount_id());
            String tampungname = takeakun.get(0).getAccount_name();
            String tampungdesc = takeakun.get(0).getDescription();
            String tampungdate = hasiltanggal.getText().toString();

            DatabaseHelper dbHelper = DatabaseHelper.getDB(ActivityTambah.this);
            dbHelper.AssistAkun().deleteAll();
            modelakun masukini = new modelakun(tampungid, tampungname, tampungdesc, String.valueOf(jumlah), tampungdate);
            dbHelper.AssistAkun().addto(masukini);

            new crudforbalance(ActivityTambah.class, this, 1).execute(tampungid, tampungname, tampungdesc, String.valueOf(jumlah), tampungdate);

            namapengeluaran.setText("");
            jumlahpengeluaran.setText("");
            mediapembayaran.setText("");
            quantitypengeluaran.setText("");
            deskripsipengeluaran.setText("");
            hasiltanggal.setText("");
        }
    }


}