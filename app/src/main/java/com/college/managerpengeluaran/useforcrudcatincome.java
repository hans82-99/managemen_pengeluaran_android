package com.college.managerpengeluaran;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class useforcrudcatincome extends AsyncTask<String, Void, String> {
    private Class<inccatAdapter> inccatAdapter;
    private DialogInterface.OnClickListener onClickListener;
    private View.OnClickListener onClickListener1;
    private int i;
    private int flagini;
    //private static final String BASE_URL = "http://10.0.2.2:80/Expense_Manager/";
    //private static final String BASE_URL = "http://192.168.1.13/Expense_Manager/";
    private static final String BASE_URL = "https://mobilekuti2022.web.id/Expense_Manager/";
    HttpURLConnection conn = null;

    public useforcrudcatincome(Class<inccatAdapter> inccatAdapterClass, View.OnClickListener onClickListener, int i) {
        this.inccatAdapter =  inccatAdapterClass;
        this.onClickListener1 = onClickListener;
        this.flagini = i;
    }

    public useforcrudcatincome(Class<com.college.managerpengeluaran.inccatAdapter> inccatAdapterClass, DialogInterface.OnClickListener onClickListener, int i) {
        this.inccatAdapter =  inccatAdapterClass;
        this.onClickListener = onClickListener;
        this.flagini = i;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = "";
        if (flagini == 0) {
            try {
                String income_category_id = (String) arg0[0];

                //String link = "https://mobilekuti2022.web.id/Expense_Manager/hapuscatfromdb.php?expense_category_id=" + expense_category_id;
                String link = BASE_URL +"hapusincat.php?expense_category_id=" + income_category_id;
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
                String income_category_id = (String) arg0[0];
                String income_category_name = (String) arg0[1];

                //URL url = new URL("https://mobilekuti2022.web.id/Expense_Manager/updatecatdb.php");
                URL url = new URL(BASE_URL+"updatecatdb.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = URLEncoder.encode("income_category_id", "UTF-8") + "=" + URLEncoder.encode(income_category_id, "UTF-8");
                data += "&" + URLEncoder.encode("income_category_name", "UTF-8") + "=" + URLEncoder.encode(income_category_name, "UTF-8");

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
