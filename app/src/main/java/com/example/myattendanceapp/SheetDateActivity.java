package com.example.myattendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myattendanceapp.Adapter.MonthAdapter;
import com.example.myattendanceapp.Model.Month;
import com.example.myattendanceapp.Model.Student;
import com.example.myattendanceapp.Utils.Ustil;
import com.example.myattendanceapp.retrofit.ApiAttendance;
import com.example.myattendanceapp.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SheetDateActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Month> listdate;
    Toolbar toolbar;
    MonthAdapter adapter;
    ApiAttendance apiAttendance;
    private int[] arrayId;
    private String s_id;
    private String[] arrayName;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        anhXa();
        getToolbar();
        getData();
        getDate();
        getItem();
    }

    private void getToolbar() {
        TextView title = toolbar.findViewById(R.id.title);
        TextView subtitle = toolbar.findViewById(R.id.detail);
        ImageView back = toolbar.findViewById(R.id.back);
        ImageView save = toolbar.findViewById(R.id.save);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setVisibility(View.GONE);
        title.setText("Attendance Record");
        Intent intent = getIntent();
        subtitle.setText(intent.getStringExtra("nameClass"));
    }

    private void getItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailSheetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("month",listdate.get(position).getDate());
                bundle.putIntArray("arrayId",arrayId);
                bundle.putStringArray("arrayName",arrayName);
                bundle.putString("s_id",s_id);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        arrayId = bundle.getIntArray("arrayId");
        arrayName = bundle.getStringArray("arrayName");
    }

    private void getDate() {
        listdate = new ArrayList<>();
        Intent intent = getIntent();
        s_id = intent.getStringExtra("s_id");
        compositeDisposable.add(apiAttendance.getMonth(Integer.parseInt(s_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        monthModel -> {
                            if(monthModel.isSuccess()){
                                List<Month> listdate2 = (ArrayList<Month>) monthModel.getResult();
                                Collections.sort(listdate2, new Comparator<Month>() {
                                    @Override
                                    public int compare(Month o1, Month o2) {
                                        return o2.getDate().compareTo(o1.getDate());
                                    }
                                });
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    listdate2.forEach(e -> Log.e("mot",e.getDate()));
                                }
                                for(int i=0;i<listdate2.size();i++){
                                    if(i==0) listdate.add(monthModel.getResult().get(i));
                                    else {
                                        if (listdate2.get(i - 1).getDate().compareTo(listdate2.get(i).getDate()) != 0) {
                                            listdate.add(monthModel.getResult().get(i));
                                        }
                                    }
                                }
                                adapter = new MonthAdapter(SheetDateActivity.this, R.layout.item_month,listdate);
                                listView.setAdapter(adapter);
                            }
                            else Toast.makeText(getApplicationContext(), "Chưa có", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void anhXa() {
        listView = findViewById(R.id.list);
        toolbar = findViewById(R.id.toolbar);
        apiAttendance = RetrofitClient.getInstance(Ustil.BASE_URL).create(ApiAttendance.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}