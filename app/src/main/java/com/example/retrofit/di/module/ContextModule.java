package com.example.retrofit.di.module;

import android.content.Context;

import com.example.retrofit.di.quilifier.ApplicationContext;
import com.example.retrofit.di.scop.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private final Context context;
    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context context() {
        return context;
    }
}
