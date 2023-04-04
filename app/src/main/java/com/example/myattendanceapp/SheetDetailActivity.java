package com.example.myattendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myattendanceapp.Utils.Ustil;
import com.example.myattendanceapp.retrofit.ApiAttendance;
import com.example.myattendanceapp.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SheetDetailActivity extends AppCompatActivity {
    private int[] arrayId;
    private String[] arrayName;
    private String month,s_id;
    Toolbar toolbar;
    ApiAttendance apiAttendance;
    CompositeDisposable compositeDisposable;
    TableLayout tableLayout;
    String x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
        anhXa();
        getData();
        getToolbar();
    }

    private void getToolbar() {
        TextView title = toolbar.findViewById(R.id.title);
        TextView subtitle = toolbar.findViewById(R.id.detail);
        ImageView back = toolbar.findViewById(R.id.back);
        ImageView save = toolbar.findViewById(R.id.save);
        save.setVisibility(View.GONE);
        subtitle.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(month);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        arrayId = bundle.getIntArray("arrayId");
        arrayName = bundle.getStringArray("arrayName");
        month = bundle.getString("month");
        s_id = bundle.getString("s_id");
        int DAY_IN_MONTH = getDayInMonth(month);

        int n = arrayId.length+1;
        TableRow[] row = new TableRow[n];
        TextView[] id = new TextView[n];
        TextView[] name = new TextView[n];
        TextView[][] status = new TextView[n][DAY_IN_MONTH+1];

        for(int i=0;i<n;i++){
            id[i] = new TextView(this);
            name[i] = new TextView(this);
            for(int j=1;j<=DAY_IN_MONTH;j++){
                status[i][j] = new TextView(this);
            }
        }

        id[0].setText("ID");
        id[0].setTypeface(id[0].getTypeface(), Typeface.BOLD);
        name[0].setText("Name");
        name[0].setTypeface(name[0].getTypeface(), Typeface.BOLD);
        for(int j=1;j<=DAY_IN_MONTH;j++){
            if(j<10) status[0][j].setText(j+"  ");
            else status[0][j].setText(j+"");
            status[0][j].setTypeface(status[0][j].getTypeface(), Typeface.BOLD);
        }

        for(int i=1;i<n;i++){
            id[i].setText(String.valueOf(arrayId[i-1]));
            name[i].setText(arrayName[i-1]+"");
            for(int j=1;j<=DAY_IN_MONTH;j++){
                String day = String.valueOf(j);
                if(day.length()==1) day = "0"+day;
                String date = month+"-"+day;
                int finalI = i;
                int finalJ = j;
                Log.d("test",s_id+" "+arrayId[i-1]+" "+date);
                compositeDisposable.add(apiAttendance.getStatus(s_id,arrayId[i-1]+"",date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                statusModel -> {
                                    if(statusModel.isSuccess()){
                                        x = statusModel.getStatus();
                                        status[finalI][finalJ].setText(x);
                                        if(status[finalI][finalJ].getText().toString().compareTo("F")==0) {
                                            status[finalI][finalJ].setBackgroundColor(Color.parseColor("#FBD7BC"));
                                            }
                                    } else {
                                        status[finalI][finalJ].setText("");
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
        }
        for(int i=0;i<n;i++){
            row[i] = new TableRow(this);
            if(i%2==0) row[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else row[i].setBackgroundColor(Color.parseColor("#E4E4E4"));
            id[i].setPadding(20,16,20,16);
            id[i].setTextSize(19);
            name[i].setPadding(20,16,20,16);
            name[i].setTextSize(19);

            row[i].addView(id[i]);
            row[i].addView(name[i]);

            for(int j=1;j<=DAY_IN_MONTH;j++){
                status[i][j].setPadding(16,20,20,16);
                status[i][j].setTextSize(19);
                status[i][j].setGravity(Gravity.CENTER);
                row[i].addView(status[i][j]);
            }
            tableLayout.addView(row[i]);
        }
    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.valueOf(month.substring(5));
        int year1 = Integer.valueOf(month.substring(0,4));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,year1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void anhXa() {
        tableLayout = findViewById(R.id.table);
        toolbar = findViewById(R.id.toolbar);
        compositeDisposable = new CompositeDisposable();
        apiAttendance = RetrofitClient.getInstance(Ustil.BASE_URL).create(ApiAttendance.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}