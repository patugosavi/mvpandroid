package com.example.retrofit.ui.login;

import com.example.retrofit.ui.showprojectlist.ProjectModel;
import com.example.retrofit.ui.uploadimage.ImageModel;

import java.util.List;

public interface MainView {

    void showProgressBar();

    void hideProgressBar();

//    void onSuccess(String a);
      void onFailure(String onError);

    void DoLoginOnSuccess(List<MainModel> mainModelList);

    void getProjectList(List<ProjectModel> projectModelList);


    void onFormUploded(List<ImageModel> imageModelList);
}
