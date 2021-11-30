package com.example.retrofit.ui.login;

import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import com.example.retrofit.network_service.ApiInterface;
import com.example.retrofit.ui.showprojectlist.ProjectListResponse;
import com.example.retrofit.ui.uploadimage.ImageResponse;
import com.example.retrofit.ui.uploadimage.RetrofitInstance;
import com.example.retrofit.utils.ErrorUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {


    private static final String TAG = "MainPresenter";
    private MainView mainView;
    private ApiInterface apiInterface;
    private Disposable disposable;

    public MainPresenter(MainView mainView, ApiInterface apiInterface) {
        this.mainView = mainView;
        this.apiInterface = apiInterface;
    }

    public void dispose() {

        if (disposable != null)
            disposable.dispose();
    }

    public void getProjectList() {
//        if (mainView!=null)
//            mainView.showProgressBar();
        Observable<ProjectListResponse> observable = apiInterface.getProjectList();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProjectListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ProjectListResponse projectListResponse) {
                        if (mainView != null) {
                            mainView.getProjectList(projectListResponse.getProjectModelList());
//                            staffView.hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        staffView.hideProgressBar();
                        mainView.onFailure(ErrorUtil.onError(e));
                    }

                    @Override
                    public void onComplete() {
//                        if (staffView!=null)
//                            staffView.hideProgressBar();
                    }
                });
    }

    public void uploadImage(String project_name, File imageFile) {

//        RequestBody project_name = RequestBody.create(MultipartBody.FORM, "project_name");
        RequestBody projectname = RequestBody.create(MediaType.parse("text/plain"), project_name);

//        MultipartBody.Part project_image = MultipartBody.Part.createFormData("image", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));

        RequestBody requestFile =RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
        MultipartBody.Part project_image =MultipartBody.Part.createFormData("project_image",imageFile.getName(),requestFile);

        if(mainView!=null){
            Observable<ImageResponse> observable =apiInterface.uploadImage(projectname,project_image);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ImageResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable=d;
                        }

                        @Override
                        public void onNext(ImageResponse imageResponse) {
                            if(mainView!=null)
                                mainView.onFormUploded(imageResponse.getImageModelList());
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(mainView!=null){
                                mainView.onFailure(ErrorUtil.onError(e));
                            }
                        }

                        @Override
                        public void onComplete() { }
                    });
        }
    }




}
