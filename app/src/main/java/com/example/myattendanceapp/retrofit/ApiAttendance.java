package com.example.myattendanceapp.retrofit;

import com.example.myattendanceapp.Model.ClassItemModel;
import com.example.myattendanceapp.Model.GiaoVienModel;
import com.example.myattendanceapp.Model.KtSave;
import com.example.myattendanceapp.Model.MonthModel;
import com.example.myattendanceapp.Model.SaveModel;
import com.example.myattendanceapp.Model.StatusGroupModel;
import com.example.myattendanceapp.Model.StatusModel;
import com.example.myattendanceapp.Model.StudentItemModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiAttendance {
    @POST("dangNhapUser.php")
    @FormUrlEncoded
    Observable<GiaoVienModel> setDangNhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("danhSachLop.php")
    @FormUrlEncoded
    Observable<ClassItemModel> getListLop(
            @Field("t_id") int t_id
    );

    @POST("danhSachStudent.php")
    @FormUrlEncoded
    Observable<StudentItemModel> getListStudent(
            @Field("s_id") int s_id
    );

    @POST("putStudent.php")
    @FormUrlEncoded
    Observable<SaveModel> putListStudent(
            @Field("listStatus") String listStudent
    );

    @POST("ktsave.php")
    @FormUrlEncoded
    Observable<KtSave> ktSave(
            @Field("s_id") String s_id,
            @Field("date_time") String date_time
    );

    @POST("getDate.php")
    @FormUrlEncoded
    Observable<MonthModel> getMonth(
           @Field("s_id") int s_id
    );

    @POST("getStatus.php")
    @FormUrlEncoded
    Observable<StatusModel> getStatus(
            @Field("s_id") String s_id,
            @Field("st_id") String st_id,
            @Field("date_time") String date_time
    );

    @POST("getStatusGroup.php")
    @FormUrlEncoded
    Observable<StatusGroupModel> getStatusGroup(
            @Field("s_id") String s_id,
            @Field("date_time") String date_time
    );
}
