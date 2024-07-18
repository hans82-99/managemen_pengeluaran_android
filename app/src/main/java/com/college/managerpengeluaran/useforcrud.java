package com.college.managerpengeluaran;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class useforcrud extends AsyncTask<String, Void, String> {
    private Class<expcatAdapter> iniexpcatadapt;
    private DialogInterface.OnClickListener onClickListener;
    private int i;
    private int flagini;

    public useforcrud(Class<expcatAdapter> expcatAdapterClass, DialogInterface.OnClickListener onClickListener, int i) {
        this.iniexpcatadapt = expcatAdapterClass;
        this.onClickListener = onClickListener;
        this.i = i;
    }

    HttpURLConnection conn = null;

    @Override
    protected String doInBackground(String... arg0) {
        String result = "";
        if (flagini == 0) {
            try {
                String expense_category_id = (String) arg0[0];
                System.out.println(expense_category_id);

                String link = "http://10.0.2.2:80/Expense_Manager/hapuscatfromdb.php?expense_category_id=" + expense_category_id;
                System.out.println(link);
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
        }
        return result;
    }

    protected void onPostExecute (String result) {
        System.out.print(result);
    }
}