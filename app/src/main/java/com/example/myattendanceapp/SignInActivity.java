package com.example.myattendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myattendanceapp.Model.GiaoVien;
import com.example.myattendanceapp.Utils.ReferenceManager;
import com.example.myattendanceapp.Utils.Ustil;
import com.example.myattendanceapp.retrofit.ApiAttendance;
import com.example.myattendanceapp.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    EditText email,pass;
    Button xacNhan;
    ReferenceManager manager;
    ApiAttendance apiAttendance;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        anhXa();
        getXacNhan();
    }
    private void getXacNhan() {
        if(manager.getString("iduser")!=null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                if(isValidSignUpDetails()){
                    compositeDisposable.add(apiAttendance.setDangNhap(email1,pass1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    giaoVienModel -> {
                                        if(giaoVienModel.isSuccess()){
                                            showToast(giaoVienModel.getMessage());
                                            GiaoVien giaoVien = giaoVienModel.getResult().get(0);
                                            manager.putString("email",giaoVien.getEmail());
                                            manager.putString("name",giaoVien.getTeacher_name());
                                            manager.putString("pass",giaoVien.getPassword());
                                            manager.putString("iduser", giaoVien.getT_id()+"");
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else showToast(giaoVienModel.getMessage());
                                    },
                                    throwable -> {
                                        showToast(throwable.getMessage()+"");
                                    }
                            ));
                }
            }
        });
    }

    private Boolean isValidSignUpDetails() {
        if (email.getText().toString().trim().isEmpty()) {
            showToast("Nhập email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast("Nhập sai email");
            return false;
        } else if (pass.getText().toString().trim().isEmpty()) {
            showToast("Nhập password");
            return false;
        } else return true;
    }

    private void showToast(String value) {
        Toast.makeText(SignInActivity.this, value+"", Toast.LENGTH_SHORT).show();
    }

    private void anhXa() {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        xacNhan = findViewById(R.id.xacNhan);
        manager = new ReferenceManager(getApplicationContext());
        apiAttendance = RetrofitClient.getInstance(Ustil.BASE_URL).create(ApiAttendance.class);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}