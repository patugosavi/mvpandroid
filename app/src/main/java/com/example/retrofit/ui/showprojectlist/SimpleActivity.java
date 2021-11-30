package com.example.retrofit.ui.showprojectlist;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.retrofit.R;
import com.example.retrofit.baseclass.BaseActivity;
import com.example.retrofit.di.component.ApplicationComponent;
import com.example.retrofit.network_service.ApiInterface;
import com.example.retrofit.ui.login.MainPresenter;
import com.example.retrofit.ui.login.MainView;
import com.example.retrofit.ui.uploadimage.ImageModel;
import com.example.retrofit.utils.BaseApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SimpleActivity extends BaseActivity implements MainView , View.OnClickListener{

     RecyclerView projectRecycler;
    List<ProjectModel> projectModelList=new ArrayList<>();

    ProjectAdapter projectAdapter;

    @Inject
    ApiInterface apiInterface;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        getSupportActionBar().hide();
        presenter=new MainPresenter(this,apiInterface);
        projectRecycler = findViewById(R.id.productRecycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        projectRecycler.setLayoutManager(layoutManager);
        projectRecycler.setItemAnimator(new DefaultItemAnimator());
        projectAdapter = new ProjectAdapter(this, projectModelList/*,this*/);
        projectRecycler.setHasFixedSize(true);

//call project list api
        presenter.getProjectList();

        setChildActivity(true);

    }

    @Override
    protected void injectDependencies(BaseApp baseApp, ApplicationComponent component) {
component.inject(this);
    }

    private void adapterOnClick() {
        projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onAdapterClick(ProjectModel projectModel, int position) {
//                Toast.makeText(SimpleActivity.this, "Click..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ShowProjectDataActivity.class).putExtra("SHOW_PROJECT_DATA", projectModel));
            }
        });
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
        if(projectModelList!=null)
        this.projectModelList = projectModelList;
        projectAdapter.getProjectList(projectModelList);
        projectRecycler.setAdapter(projectAdapter);

        adapterOnClick();
    }

    @Override
    public void onFormUploded(List<ImageModel> imageModelList) {

    }



    @Override
    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.txtProject:
//                String imageUrl = String.valueOf(view.getTag());
//                Toast.makeText(SimpleActivity.this, "click", Toast.LENGTH_SHORT).show();
//                break;
//
//        }
    }
}