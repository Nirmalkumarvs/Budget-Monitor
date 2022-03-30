package com.example.expendic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.time.LocalDate;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity {

    TextView tvmilk,tvtrans,tvgross,tvfast,tvveg,tvcosmetics;
    Button btnadd;
    ProgressDialog loading;
    String lastdate,currentdate,gmilk,gtrans,ggross,gveg,gfast,gcosmetics,gaddcount;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        tvmilk = (TextView)findViewById(R.id.tvmilk_id);
        tvtrans = (TextView)findViewById(R.id.tvtrans_id);
        tvgross = (TextView)findViewById(R.id.tvgross_id);
        tvfast = (TextView)findViewById(R.id.tvfast_id);
        tvveg = (TextView)findViewById(R.id.tvveg_id);
        tvcosmetics = (TextView)findViewById(R.id.tvcosmetics_id);
        btnadd=(Button)findViewById(R.id.addval_id);
        LocalDate myod = LocalDate.now();
        currentdate=myod.toString();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItems();
              //  addItemToSheet();

                  //  Toast.makeText(AddItem.this, lastdate+"---------"+currentdate, Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void getItems() {

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void parseItems(String jsonResposnce) {
         String date="0";
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                     gmilk      = jo.getString("milk");
                     gtrans     = jo.getString("trans");
                     ggross     = jo.getString("gross");
                     gveg       = jo.getString("veg");
                     gfast      = jo.getString("fast");
                     gcosmetics = jo.getString("cosmetics");
                     gaddcount  = jo.getString("addcount");
                     date =jo.getString("date");

                //Toast.makeText(this, date, Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        lastdate=date.substring(0,10);
      //  Toast.makeText(AddItem.this, lastdate+"---------"+currentdate, Toast.LENGTH_SHORT).show();
        loading.dismiss();
        if(lastdate.equals(currentdate))
        {
            addItemToSheet1();
        }
        else {
            addItemToSheet();
        }

    }

    private void  addItemToSheet() {

        if(tvmilk.getText().toString().isEmpty() || tvtrans.getText().toString().isEmpty() ||  tvgross.getText().toString().isEmpty() || tvveg.getText().toString().isEmpty()
       || tvfast.getText().toString().isEmpty() || tvcosmetics.getText().toString().isEmpty() ) {
            Toast.makeText(AddItem.this,"Fill all values",Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String milk,trans,gross,veg,fast,cosmetics,addcount;
     /*
             milk = Integer.toString(Integer.parseInt(tvmilk.getText().toString().trim())+Integer.parseInt(gmilk));
            trans = Integer.toString(Integer.parseInt(tvtrans.getText().toString().trim())+Integer.parseInt(gtrans));
            gross = Integer.toString(Integer.parseInt(tvgross.getText().toString().trim())+Integer.parseInt(ggross));
            veg = Integer.toString(Integer.parseInt(tvveg.getText().toString().trim())+Integer.parseInt(gveg));
            fast = Integer.toString(Integer.parseInt(tvfast.getText().toString().trim())+Integer.parseInt(gfast));
            cosmetics = Integer.toString(Integer.parseInt(tvcosmetics.getText().toString().trim())+Integer.parseInt(gcosmetics));
*/
                    milk = tvmilk.getText().toString().trim();
                    trans = tvtrans.getText().toString().trim(); //https://script.google.com/macros/s/AKfycbz6ZFR5_rIrFUi5wFzCp6ndn7yzs9a24g-hlwea79fA8elF5do/exec
                    gross = tvgross.getText().toString().trim();
                    veg = tvveg.getText().toString().trim();
                    fast = tvfast.getText().toString().trim();
                    cosmetics = tvcosmetics.getText().toString().trim();
                    addcount = "1";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec",
                new Response.Listener<String>() {     //https://script.google.com/macros/s/AKfycbwXdj74r_BUJS0Jr_dWV5rhJhh9ElCmfIunaSz3JJKX-BKtAtc/exec
                    @Override //https://script.google.com/macros/s/AKfycbx2K17lWSIR29QsCl47Sj3oDjHLVvmWuEH2_Ip4/exec
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(AddItem.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),EntryPage.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params

                parmas.put("action","addItem");
                parmas.put("milk",milk);
                parmas.put("trans",trans);
                parmas.put("gross",gross);
                parmas.put("veg",veg);
                parmas.put("fast",fast);
                parmas.put("cosmetics",cosmetics);
                parmas.put("addcount",addcount);


                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);



    }

    private void deldata()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec?action=delItem",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        // Toast.makeText(AddItem.this, "Latest data is deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(AddItem.this);
        queue.add(stringRequest);
    }
    private void  addItemToSheet1() {

        if(tvmilk.getText().toString().isEmpty() || tvtrans.getText().toString().isEmpty() ||  tvgross.getText().toString().isEmpty() || tvveg.getText().toString().isEmpty()
                || tvfast.getText().toString().isEmpty() || tvcosmetics.getText().toString().isEmpty() ) {
            Toast.makeText(AddItem.this,"Fill all values",Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String milk,trans,gross,veg,fast,cosmetics,addcount;

             milk = Integer.toString(Integer.parseInt(tvmilk.getText().toString().trim())+Integer.parseInt(gmilk));
            trans = Integer.toString(Integer.parseInt(tvtrans.getText().toString().trim())+Integer.parseInt(gtrans));
            gross = Integer.toString(Integer.parseInt(tvgross.getText().toString().trim())+Integer.parseInt(ggross));
            veg = Integer.toString(Integer.parseInt(tvveg.getText().toString().trim())+Integer.parseInt(gveg));
            fast = Integer.toString(Integer.parseInt(tvfast.getText().toString().trim())+Integer.parseInt(gfast));
            cosmetics = Integer.toString(Integer.parseInt(tvcosmetics.getText().toString().trim())+Integer.parseInt(gcosmetics));
            addcount = Integer.toString(1+Integer.parseInt(gaddcount));

           deldata();
           try {
               Thread.sleep(3000);
           }
           catch(InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec",
                new Response.Listener<String>() {     //https://script.google.com/macros/s/AKfycbwXdj74r_BUJS0Jr_dWV5rhJhh9ElCmfIunaSz3JJKX-BKtAtc/exec
                    @Override //https://script.google.com/macros/s/AKfycbx2K17lWSIR29QsCl47Sj3oDjHLVvmWuEH2_Ip4/exec
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(AddItem.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),EntryPage.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params

                parmas.put("action","addItem");
                parmas.put("milk",milk);
                parmas.put("trans",trans);
                parmas.put("gross",gross);
                parmas.put("veg",veg);
                parmas.put("fast",fast);
                parmas.put("cosmetics",cosmetics);
                parmas.put("addcount",addcount);

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);



    }
}