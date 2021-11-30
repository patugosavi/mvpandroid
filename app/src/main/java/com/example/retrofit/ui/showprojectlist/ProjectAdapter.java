package com.example.retrofit.ui.showprojectlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofit.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.Viewholder> {

    Context context;
    List<ProjectModel> projectModelList;
    private OnItemClickListener listener;
//    private View.OnClickListener onClickListener;

    public ProjectAdapter(Context context, List<ProjectModel> projectModelList/*,View.OnClickListener onClickListener*/) {
        this.context = context;
        this.projectModelList = projectModelList;
//        this.onClickListener=onClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ProjectModel model = projectModelList.get(position);
        holder.txtProject.setText(model.getProName());

        String projectname=model.getProName();
        String imageurl="https://personalexpo.pythonanywhere.com"+model.getProImg();

        Glide.with(context)
                .load(imageurl)
                .into(holder.iv_image);

        //using adapterclick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAdapterClick(model,position);
            }
        });


        //using clicklistener
//        holder.txtProject.setTag(projectname);
//        holder.txtProject.setOnClickListener(onClickListener);


    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    public void getProjectList(List<ProjectModel> projectModelList) {
        this.projectModelList=  projectModelList;
        notifyDataSetChanged();
    }


    public class Viewholder  extends RecyclerView.ViewHolder{
        @BindView(R.id.txtProduct)TextView txtProject;
        @BindView(R.id.iv_image)ImageView iv_image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public interface OnItemClickListener {
        void onAdapterClick(ProjectModel projectModel, int position);
    }

    public void setOnItemClickListener(ProjectAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

