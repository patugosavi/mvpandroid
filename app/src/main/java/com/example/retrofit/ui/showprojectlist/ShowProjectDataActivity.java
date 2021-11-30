package com.example.retrofit.ui.showprojectlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.retrofit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowProjectDataActivity extends AppCompatActivity {

    @BindView(R.id.tv_projectname)
    TextView tv_projectname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project_data);

        ButterKnife.bind(this);

        Intent intent=getIntent();
        ProjectModel model=intent.getParcelableExtra("SHOW_PROJECT_DATA");
        if (model!=null) {
            tv_projectname.setText(""+model.getProName());
        }
    }
}