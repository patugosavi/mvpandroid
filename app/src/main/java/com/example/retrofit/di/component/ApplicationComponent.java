package com.example.retrofit.di.component;


import android.content.SharedPreferences;

import com.example.retrofit.di.module.RetrofitModule;
import com.example.retrofit.di.module.SharedPrefModule;
import com.example.retrofit.di.scop.ApplicationScope;
import com.example.retrofit.network_service.ApiInterface;
import com.example.retrofit.ui.login.MainActivity;
import com.example.retrofit.ui.showprojectlist.SimpleActivity;
import com.example.retrofit.ui.uploadimage.UploadImageActivity;

import dagger.Component;

@ApplicationScope
@Component(modules = {RetrofitModule.class, SharedPrefModule.class})
public interface ApplicationComponent {

    ApiInterface getNetworkService();

    SharedPreferences sharedPrefences();

    void inject(MainActivity mainActivity);

    void inject(SimpleActivity simpleActivity);

    void inject(UploadImageActivity uploadImageActivity);
}

