package com.example.retrofit.ui.showprojectlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("project_data")
    @Expose
    private List<ProjectModel> projectModelList = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProjectModel> getProjectModelList() {
        return projectModelList;
    }

    public void setProjectModelList(List<ProjectModel> projectModelList) {
        this.projectModelList = projectModelList;
    }
}
