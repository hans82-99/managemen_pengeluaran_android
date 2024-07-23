package com.college.managerpengeluaran;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class crudforbalance extends AsyncTask<String, Void, String> {
    private Class<ActivityTambah> activityTambahClass;
    private Class<TransactionAdapter> transactionAdapterClass;
    private Class<com.college.managerpengeluaran.transactionCrud> transactionCrudClass;
    private Class<ActivityPemasukan> activityPemasukanClass;
    private ActivityPemasukan.inputincome inputincome;
    private transactionCrud transactionCrud;
    private ActivityTambah.inputexpense inputexpense;
    private int flagini;
    HttpURLConnection conn = null;
    //private static final String BASE_URL = "http://192.168.1.13/Expense_Manager/";
    //private static final String BASE_URL = "http://10.0.2.2:80/Expense_Manager/";
    private static final String BASE_URL = "https://mobilekuti2022.web.id/Expense_Manager/";


    public crudforbalance(Class<ActivityTambah> activityTambahClass, ActivityTambah.inputexpense inputexpense, int i) {
        this.activityTambahClass = activityTambahClass;
        this.inputexpense = inputexpense;
        this.flagini = i;
    }

    public crudforbalance(Class<TransactionAdapter> transactionAdapterClass, transactionCrud transactionCrud, int i) {
        this.transactionAdapterClass = transactionAdapterClass;
        this.transactionCrud = transactionCrud;
        this.flagini = i;
    }

    public crudforbalance(Class<com.college.managerpengeluaran.transactionCrud> transactionCrudClass, Class<TransactionAdapter> transactionAdapterClass, com.college.managerpengeluaran.transactionCrud transactionCrud, int i) {
        this.transactionCrudClass = transactionCrudClass;
        this.transactionAdapterClass = transactionAdapterClass;
        this.transactionCrud = transactionCrud;
        this.flagini = i;
    }

    public crudforbalance(Class<ActivityPemasukan> activityPemasukanClass, ActivityPemasukan.inputincome inputexpense, int i) {
        this.activityTambahClass = activityTambahClass;
        this.inputincome = inputexpense;
        this.flagini = i;
    }

    protected String doInBackground(String... arg0) {
        String result = "";
        if (flagini == 0) {
            try {
                String account_id = (String) arg0[0];
                String account_name = (String) arg0[1];
                String description = (String) arg0[2];
                String account_balance = (String) arg0[3];
                String date = (String) arg0[4];

                //String link = "https://mobilekuti2022.web.id/Expense_Manager/updatebalkedb.php?id=" + account_id;\
                String link = BASE_URL + "updatebalkedb.php?id=" + account_id;
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
        } else if (flagini == 1) {
            try {
                String account_id = (String) arg0[0];
                String account_name = (String) arg0[1];
                String description = (String) arg0[2];
                String initial_balance = (String) arg0[3];
                String date = (String) arg0[4];

                //URL url = new URL("https://mobilekuti2022.web.id/Expense_Manager/updatebalkedb.php");
                URL url = new URL(BASE_URL +"updatebalkedb.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = URLEncoder.encode("account_id", "UTF-8") + "=" + URLEncoder.encode(account_id, "UTF-8");
                data += "&" + URLEncoder.encode("account_name", "UTF-8") + "=" + URLEncoder.encode(account_name, "UTF-8");
                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
                data += "&" + URLEncoder.encode("initial_balance", "UTF-8") + "=" + URLEncoder.encode(initial_balance, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");

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
        System.out.print(result);
    }
}
