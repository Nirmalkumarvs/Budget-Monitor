package com.example.expendic;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListItem extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ListView listView;
    SimpleAdapter adapter;
    ProgressDialog loading;
    EditText editTextSearchItem;
    int save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        listView = (ListView)findViewById(R.id.lv_items);
        listView.setOnItemClickListener(this);
        editTextSearchItem=(EditText)findViewById(R.id.et_search);

        getItems();

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

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String milk = jo.getString("milk");
                String trans =   jo.getString("trans");
                String gross =      jo.getString("gross");
                String veg =      jo.getString("veg");
                String fast =      jo.getString("fast");
                String cosmetics = jo.getString("cosmetics");
                String addcount = jo.getString("addcount");
                String date =jo.getString("date");
                int total=0;
                total= Integer.parseInt(milk) + Integer.parseInt(trans) + Integer.parseInt(gross) + Integer.parseInt(veg) + Integer.parseInt(fast) + Integer.parseInt(cosmetics) ;
                 milk = "Milk : "   +     milk;
                 trans = "Transport: "+   trans;
                 gross = "Grossery: "+    gross;
                 veg = "Vegetables: "+   veg;
                 fast = "Fastfood: "+      fast;
                 cosmetics = "Cosmetics: "+cosmetics;
                 date = "Date: "+          date;
                 addcount = "Addcount: "+          addcount;
                 date=date.substring(0,16);
                 String totexp="Total exp: "+Integer.toString(total);
                 save+=total;

                HashMap<String, String> item = new HashMap<>();

                item.put("milk",milk);
                item.put("trans",trans);
                item.put("gross",gross);
                item.put("veg",veg);
                item.put("fast",fast);
                item.put("cosmetics",cosmetics);
                item.put("date",date);
                item.put("totexp",totexp);
                item.put("addcount",addcount);
                list.add(item);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(this,list,R.layout.itemrow,
                new String[]{"date","milk","trans","veg","fast","gross","cosmetics","totexp","addcount"},new int[]{R.id.irdate_id,R.id.irmilk_id,R.id.irtrans_id,R.id.irveg_id,R.id.irfast_id,R.id.irgross_id,R.id.ircosmetics_id,R.id.irtotexp_id,R.id.iraddcount_id});


        listView.setAdapter(adapter);
        loading.dismiss();


        editTextSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ListItem.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ItemDetails.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);

       String milk = map.get("milk").toString();
       String trans = map.get("trans").toString();
       String gross = map.get("gross").toString();
       String veg = map.get("veg").toString();
       String fast = map.get("fast").toString();
       String cosmetics = map.get("cosmetics").toString();
       String date = map.get("date").toString();
       String totexp = map.get("totexp").toString();
       String addcount = map.get("addcount").toString();

       intent.putExtra("milk",milk);
       intent.putExtra("trans",trans);
       intent.putExtra("gross",gross);
       intent.putExtra("veg",veg);
       intent.putExtra("fast",fast);
       intent.putExtra("cosmetics",cosmetics);
       intent.putExtra("date",date);
       intent.putExtra("totexp",totexp);
       intent.putExtra("addcount",addcount);


        startActivity(intent);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
