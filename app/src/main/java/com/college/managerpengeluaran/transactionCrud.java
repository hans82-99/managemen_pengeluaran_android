package com.college.managerpengeluaran;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class transactionCrud extends AsyncTask <String, Void, String>{
    private Class<TransactionAdapter> transactionAdapterClass;
    private DialogInterface.OnClickListener onClickListener;
    private int flagini;
    private Context context;
    private HttpURLConnection conn;
    List<modelakun> takeakun;
    DatabaseHelper dbHelper;

    public transactionCrud(Class<TransactionAdapter> transactionAdapterClass, DialogInterface.OnClickListener onClickListener, int i) {
        this.transactionAdapterClass = transactionAdapterClass;
        this.onClickListener = onClickListener;
        this.flagini = i;
        this.dbHelper = DatabaseHelper.getDB(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = "";
        if (flagini == 0) {
            try {
                takeakun = dbHelper.AssistAkun().getAkun();
                String expense_id = (String) arg0[0];

                String link = "https://mobilekuti2022.web.id/Expense_Manager/hapusexpense.php?expense_id=" + expense_id;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                result = sb.toString();

                if (result.contains("Warning")) {
                    Log.e("MyAsyntask", "Warning found in response");
                    return "Exception: Warning found in response";
                } else {
                    return result;
                }
            } catch (Exception e) {
                Log.e("MyAsyntask", "Exception occurred", e);
                return "Exception: " + e.getMessage();
            }
        }else if (flagini == 1) {
            try {
                String income_id = (String) arg0[0];

                String link = "https://mobilekuti2022.web.id/Expense_Manager/hapusincome.php?income_id=" + income_id;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                result = sb.toString();

                if (result.contains("Warning")) {
                    Log.e("MyAsyntask", "Warning found in response");
                    return "Exception: Warning found in response";
                } else {
                    return result;
                }
            } catch (Exception e) {
                Log.e("MyAsyntask", "Exception occurred", e);
                return "Exception: " + e.getMessage();
            }
        }else if (flagini == 2) {
            try {
                String expense_category_id = (String) arg0[0];
                String expense_category_name = (String) arg0[1];

                URL url = new URL("https://mobilekuti2022.web.id/Expense_Manager/updatecatdb.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = URLEncoder.encode("expense_category_id", "UTF-8") + "=" + URLEncoder.encode(expense_category_id, "UTF-8");
                data += "&" + URLEncoder.encode("expense_category_name", "UTF-8") + "=" + URLEncoder.encode(expense_category_name, "UTF-8");

                System.out.println(data);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder("");
                String line="";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                System.out.println(result);

                if (result.contains("Warning")) {
                    Log.e("MyAsyntask", "Warning found in response");
                    return "Exception: Warning found in response";
                } else {
                    return result;
                }
            } catch (Exception e) {
                Log.e("MyAsyntask", "Exception occurred", e);
                return "Exception: " + e.getMessage();
            }
        }
        return result;
    }

    protected void onPostExecute (String result) {
        if (takeakun != null && !takeakun.isEmpty()) {
            String tampungid = String.valueOf(takeakun.get(0).getAccount_id());
            String tampungname = takeakun.get(0).getAccount_name();
            String tampungdesc = takeakun.get(0).getDescription();
            String tampungjumlah = String.valueOf(takeakun.get(0).getInitial_balance());
            String tampungdate = takeakun.get(0).getDate();

            new crudforbalance(transactionCrud.class, transactionAdapterClass, this, 1).execute(tampungid, tampungname, tampungdesc, tampungjumlah, tampungdate);
        } else {
            Log.e("transactionCrud", "No data retrieved from database.");
        }
        System.out.print(result);
    }
}
