package com.example.retrofit.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.retrofit.di.quilifier.ApplicationContext;
import com.example.retrofit.di.scop.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class SharedPrefModule {

    @Provides
    @ApplicationScope
    SharedPreferences sharedPreferences(@ApplicationContext Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
