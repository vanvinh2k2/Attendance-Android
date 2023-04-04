package com.example.myattendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.example.myattendanceapp.Model.Status;
import com.example.myattendanceapp.Utils.Ustil;
import com.example.myattendanceapp.retrofit.ApiAttendance;
import com.example.myattendanceapp.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailSheetActivity extends AppCompatActivity {
    private int[] arrayId;
    private String[] arrayName;
    private String month,s_id;
    Toolbar toolbar;
    ApiAttendance apiAttendance;
    CompositeDisposable compositeDisposable;
    TableLayout tableLayout;
    List<Status> statusList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sheet);
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

        compositeDisposable.add(apiAttendance.getStatusGroup(s_id,month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        statusGroupModel -> {
                            if(statusGroupModel.isSuccess()){
                                statusList = statusGroupModel.getResult();
                                for(int i=0;i<statusList.size();i++){
                                    for(int j=1;j<n;j++) {
                                        id[j].setText(String.valueOf(arrayId[j - 1]));
                                        name[j].setText(arrayName[j - 1] + "");
                                        for (int k = 1; k <= DAY_IN_MONTH; k++) {
                                            if(arrayId[j-1]==Integer.parseInt(statusList.get(i).getSt_id()) && k == Integer.parseInt(statusList.get(i).getDate_tim())){
                                                status[j][k].setText(statusList.get(i).getStatus());
                                                if(status[j][k].getText().toString().compareTo("A")==0) {
                                                    status[j][k].setBackgroundColor(Color.parseColor("#FBD7BC"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_LONG).show();
                        }
                ));

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
        statusList = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        apiAttendance = RetrofitClient.getInstance(Ustil.BASE_URL).create(ApiAttendance.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}