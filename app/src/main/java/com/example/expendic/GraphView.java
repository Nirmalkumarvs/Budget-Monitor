package com.example.expendic;
import android.app.ProgressDialog;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
//import com.anychart.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphView extends AppCompatActivity {


    int save=0;
    ProgressDialog loading;
    int realsalary=25000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        getsalary();
        getItems();
        loading.dismiss();

    }

    private void getsalary() {
        loading = ProgressDialog.show(this, "Loading", "please wait", false, true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec?action=getsal",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parsesal(response);
                        loading.dismiss();
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

    private void getItems() {

        loading = ProgressDialog.show(this, "Loading", "please wait", false, true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbydSVOb03SFuUnnpWoWVG45uEzCS4GaAFm7pmTcsJx-QXOW5cQ/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                        loading.dismiss();
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

    private void parsesal(String jsonResposnce) {

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String sala = jo.getString("salary");
                realsalary = Integer.parseInt(sala);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loading.dismiss();
    }
    private void parseItems(String jsonResposnce) {
        int totmilk = 0;
        int tottrans = 0;
        int totgross = 0;
        int totveg = 0;
        int totfast = 0;
        int totcosmetics = 0;
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String milk = jo.getString("milk");
                String trans = jo.getString("trans");
                String gross = jo.getString("gross");
                String veg = jo.getString("veg");
                String fast = jo.getString("fast");
                String cosmetics = jo.getString("cosmetics");

                 int total = 0;
                 total = Integer.parseInt(milk) + Integer.parseInt(trans) + Integer.parseInt(gross) + Integer.parseInt(veg) + Integer.parseInt(fast) + Integer.parseInt(cosmetics);
                save+=total;
                totmilk += Integer.parseInt(milk);
                tottrans += Integer.parseInt(trans);
                totgross += Integer.parseInt(gross);
                totveg += Integer.parseInt(veg);
                totfast += Integer.parseInt(fast);
                totcosmetics += Integer.parseInt(cosmetics);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);


        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(GraphView.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Milk", totmilk));
        data.add(new ValueDataEntry("Transport", tottrans));
        data.add(new ValueDataEntry("Grossery", totgross));
        data.add(new ValueDataEntry("Fastfood", totfast));
        data.add(new ValueDataEntry("Vegetables", totveg));
        data.add(new ValueDataEntry("Cosmetics", totcosmetics));
        data.add(new ValueDataEntry("Save", realsalary-save));

        pie.data(data);

        pie.title("Expenditure of our family ");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Major provisions")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
        loading.dismiss();

    }
}

