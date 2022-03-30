package com.example.expendic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class EntryPage extends AppCompatActivity {

    Button btnadd,btngraph,btnlist,btndellast,btnupdatesalary;
    ListView listView;
    SimpleAdapter adapter;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
        btnadd=(Button)findViewById(R.id.btnadd_id);
        btnlist=(Button)findViewById(R.id.btnlist_id);
        btngraph=(Button)findViewById(R.id.btngraph_id);
        btndellast=(Button)findViewById(R.id.btndellast_id);
        btnupdatesalary=(Button)findViewById(R.id.btnupdatesalary_id);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryPage.this,AddItem.class);
                startActivity(intent);
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryPage.this,ListItem.class);
                startActivity(intent);
            }
        });

        btngraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryPage.this,GraphView.class);
                startActivity(intent);
            }
        });

        btnupdatesalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryPage.this,UpdateSalary.class);
                startActivity(intent);
            }
        });


        btndellast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading =  ProgressDialog.show(EntryPage.this,"Loading","please wait",false,true);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec?action=delItem",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                Toast.makeText(EntryPage.this, "Latest data is deleted successfully", Toast.LENGTH_SHORT).show();
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

                RequestQueue queue = Volley.newRequestQueue(EntryPage.this);
                queue.add(stringRequest);
            }
        });
    }
}