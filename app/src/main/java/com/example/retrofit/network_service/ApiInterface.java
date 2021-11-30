package com.example.retrofit.network_service;

import com.example.retrofit.ui.showprojectlist.ProjectListResponse;
import com.example.retrofit.ui.uploadimage.ImageResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {



    @GET("test_one/get_project_data")
    Observable<ProjectListResponse> getProjectList();


    @Multipart
    @POST("test_one/post_api")
//    @FormUrlEncoded
    Observable<ImageResponse> uploadImage(@Part("project_name")  RequestBody project_name,
                                          @Part MultipartBody.Part project_image);



}
