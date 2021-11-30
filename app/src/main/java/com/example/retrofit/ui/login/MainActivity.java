package com.example.retrofit.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofit.R;
import com.example.retrofit.ui.showprojectlist.ProjectModel;
import com.example.retrofit.ui.showprojectlist.SimpleActivity;
import com.example.retrofit.baseclass.BaseActivity;
import com.example.retrofit.di.component.ApplicationComponent;
import com.example.retrofit.network_service.ApiInterface;
import com.example.retrofit.ui.uploadimage.ImageModel;
import com.example.retrofit.ui.uploadimage.UploadImageActivity;
import com.example.retrofit.utils.BaseApp;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.btnProjectList) Button btnProjectList;
    @BindView(R.id.btnuploadimage)Button btnuploadimage;

    @Inject
    ApiInterface apiInterface;

    MainPresenter presenter;

    String username,password;

Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=getApplicationContext();

        ButterKnife.bind(this);
        presenter = new MainPresenter(this,apiInterface);
    }

    @Override
    protected void injectDependencies(BaseApp baseApp, ApplicationComponent component) {
        component.inject(this);
    }

    @OnClick(R.id.btnProjectList)
    public void BtnNext(){
        startActivity(new Intent(MainActivity.this, SimpleActivity.class) );
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String onError) {

    }

    @Override
    public void DoLoginOnSuccess(List<MainModel> mainModelList) {


    }

    @Override
    public void getProjectList(List<ProjectModel> projectModelList) {

    }

    @Override
    public void onFormUploded(List<ImageModel> imageModelList) {

    }


    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnuploadimage)
    public void btnuploadimage(){
        Intent intent=new Intent(MainActivity.this, UploadImageActivity.class);
        startActivity(intent);
    }
}