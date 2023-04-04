package com.example.myattendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myattendanceapp.Adapter.ClassAdapter;
import com.example.myattendanceapp.Model.ClassItem;
import com.example.myattendanceapp.Utils.ReferenceManager;
import com.example.myattendanceapp.Utils.Ustil;
import com.example.myattendanceapp.retrofit.ApiAttendance;
import com.example.myattendanceapp.retrofit.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView notify;
    ArrayList<ClassItem> classRoomList;
    ClassAdapter adapter;
    ApiAttendance apiAttendance;
    ReferenceManager manager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        getDataClass();
        setToolBar();
    }

    private void getDataClass() {
        compositeDisposable.add(apiAttendance.getListLop(Integer.parseInt(manager.getString("iduser")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        classItemModel -> {
                            if(classItemModel.isSuccess()){
                                classRoomList = (ArrayList<ClassItem>) classItemModel.getResult();
                                adapter = new ClassAdapter(classRoomList,getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }
                            else{
                                notify.setText(classItemModel.getMessage()+"");
                                notify.setVisibility(View.VISIBLE);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void setToolBar() {
        TextView title = toolbar.findViewById(R.id.title);
        title.setText(manager.getString("name"));
        toolbar.inflateMenu(R.menu.menu_logout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.logout){
                    manager.clear();
                    Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }


    private void anhXa() {
        apiAttendance = RetrofitClient.getInstance(Ustil.BASE_URL).create(ApiAttendance.class);
        recyclerView = findViewById(R.id.listClass);
        toolbar = findViewById(R.id.toolbarmain);
        manager = new ReferenceManager(getApplicationContext());
        classRoomList = new ArrayList<>();
        notify = findViewById(R.id.empty);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}