package com.example.myattendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myattendanceapp.Adapter.StudentAdapter;
import com.example.myattendanceapp.Model.ClassItem;
import com.example.myattendanceapp.Model.StatusItem;
import com.example.myattendanceapp.Model.Student;
import com.example.myattendanceapp.Model.StudentItem;
import com.example.myattendanceapp.Utils.Ustil;
import com.example.myattendanceapp.retrofit.ApiAttendance;
import com.example.myattendanceapp.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView time;
    RecyclerView recyclerView;
    List<Student> studentItemList;
    List<StudentItem> studentItems;
    StudentAdapter adapter;
    ApiAttendance apiAttendance;
    private String timeDate,idclass="",nameCalss="";
    private int[] arrayId;
    private String[] arrayName;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        anhXa();
        setToolBar();
    }

    private void getSave() {
                compositeDisposable.add(apiAttendance.putListStudent(new Gson().toJson(Ustil.listStatus))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                saveModel -> {
                                    if(saveModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(), saveModel.getResult()+"", Toast.LENGTH_SHORT).show();
                                    }else Toast.makeText(getApplicationContext(), saveModel.getResult()+"", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        ));
    }

    private void setToolBar() {
        Intent intent = getIntent();
        ClassItem room = (ClassItem) intent.getSerializableExtra("dulieu");
        TextView title = toolbar.findViewById(R.id.title);
        TextView subtitle = toolbar.findViewById(R.id.detail);
        ImageView back = toolbar.findViewById(R.id.back);
        ImageView save = toolbar.findViewById(R.id.save);
        title.setText(room.getSubject_code()+"");
        subtitle.setText(room.getSubject_name());
        nameCalss = room.getSubject_code()+" | "+room.getSubject_name();
        idclass = room.getS_id()+"";
        compositeDisposable.add(apiAttendance.getListStudent(room.getS_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        studentItemModel -> {
                            if(studentItemModel.isSuccess()){
                                studentItems = studentItemModel.getResult();
                                arrayId = new int[studentItems.size()];
                                arrayName = new String[studentItems.size()];
                                for(int i=0;i<studentItems.size();i++){
                                    arrayId[i] = studentItems.get(i).getSt_id();
                                    arrayName[i] = studentItems.get(i).getStudent_name();
                                    studentItemList.add(new Student(studentItems.get(i).getSt_id()+"",studentItems.get(i).getStudent_name()));
                                }
                                adapter = new StudentAdapter(getApplicationContext(),studentItemList);
                                recyclerView.setAdapter(adapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(StudentActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ustil.listStatus.clear();
                for (int i=0;i<studentItemList.size();i++){
                    Ustil.listStatus.add(new StatusItem(studentItems.get(i).getSt_id()+""
                            ,timeDate
                            ,studentItemList.get(i).getStatus(),idclass));
                }
                Log.d("test",new Gson().toJson(Ustil.listStatus));
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("Notification!");

                int y = 0, n = 0;
                for(int i=0;i<studentItemList.size();i++){
                    if(studentItemList.get(i).getStatus().compareTo("A")==0){
                        n++;
                    }else y++;
                }
                builder.setMessage("Present: "+y+"\nAbsent: "+n);
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                kiemTraSave();
                    }
                });
                builder.show();
            }
        });

        toolbar.inflateMenu(R.menu.menu_student);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.changtime){
                    getDiaLogTime();
                }
                if(item.getItemId() == R.id.attend){
                    getAttendance();
                }
                return false;
            }
        });
    }

    private void getAttendance() {
        Intent intent = new Intent(getApplicationContext(),SheetDateActivity.class);
        intent.putExtra("s_id",idclass);
        Bundle bundle = new Bundle();
        bundle.putIntArray("arrayId",arrayId);
        bundle.putStringArray("arrayName",arrayName);
        intent.putExtra("data", bundle);
        intent .putExtra("nameClass",nameCalss);
        startActivity(intent);
    }

    private void getDiaLogTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        int ngay=calendar1.get(Calendar.DATE);
        int thang=calendar1.get(Calendar.MONTH);
        int nam=calendar1.get(Calendar.YEAR);
        DatePickerDialog pickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar1.set(year,month,dayOfMonth);
                timeDate = simpleDateFormat.format(calendar1.getTime()).toString();
                time.setText("Time: "+timeDate);
            }
        },nam,thang,ngay);
        pickerDialog.show();
    }

    private void anhXa() {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar2 = Calendar.getInstance();
        timeDate = simpleDateFormat1.format(calendar2.getTime()).toString();
        Ustil.listStatus = new ArrayList<>();
        studentItemList = new ArrayList<>();
        studentItems = new ArrayList<>();
        time = findViewById(R.id.time);
        time.setText("Time: "+timeDate);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.listStudent);
        apiAttendance = RetrofitClient.getInstance(Ustil.BASE_URL).create(ApiAttendance.class);
    }

    private void kiemTraSave(){

        compositeDisposable.add(apiAttendance.ktSave(idclass,timeDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ktSave -> {
                            if(ktSave.isSuccess()){
                                Toast.makeText(this, "Saved already, can't save", Toast.LENGTH_SHORT).show();
                            }else {
                                getSave();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                )
        );

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}