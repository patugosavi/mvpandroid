package com.example.retrofit.ui.showprojectlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectModel implements Parcelable {

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("pro_name")
    @Expose
    private String proName;
    @SerializedName("pro_img")
    @Expose
    private String proImg;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    protected ProjectModel(Parcel in) {
        if (in.readByte() == 0) {
            pk = null;
        } else {
            pk = in.readInt();
        }
        proName = in.readString();
        proImg = in.readString();
        createdAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (pk == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pk);
        }
        dest.writeString(proName);
        dest.writeString(proImg);
        dest.writeString(createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProjectModel> CREATOR = new Creator<ProjectModel>() {
        @Override
        public ProjectModel createFromParcel(Parcel in) {
            return new ProjectModel(in);
        }

        @Override
        public ProjectModel[] newArray(int size) {
            return new ProjectModel[size];
        }
    };

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

