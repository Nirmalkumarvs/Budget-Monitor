package com.example.expendic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UpdateSalary extends AppCompatActivity {

    Button btnupdate;
    TextView tvsalary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_salary);
        btnupdate = (Button)findViewById(R.id.btnupdate_id);
        tvsalary = (TextView)findViewById(R.id.etsalary_id);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToSheet();
            }
        });
    }
    private void  addItemToSheet() {

        if(tvsalary.getText().toString().isEmpty() ) {
            Toast.makeText(UpdateSalary.this,"Fill the value",Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");

        final String salary = tvsalary.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec",
                new Response.Listener<String>() {     //https://script.google.com/macros/s/AKfycbwXdj74r_BUJS0Jr_dWV5rhJhh9ElCmfIunaSz3JJKX-BKtAtc/exec
                    @Override //https://script.google.com/macros/s/AKfycbx2K17lWSIR29QsCl47Sj3oDjHLVvmWuEH2_Ip4/exec
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(UpdateSalary.this,response,Toast.LENGTH_LONG).show();
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

                parmas.put("action","addsal");
                parmas.put("salary",salary);



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