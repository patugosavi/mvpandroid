package com.example.retrofit.ui.uploadimage;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.R;
import com.example.retrofit.baseclass.BaseActivity;
import com.example.retrofit.di.component.ApplicationComponent;
import com.example.retrofit.di.module.GlideApp;
import com.example.retrofit.network_service.ApiInterface;
import com.example.retrofit.ui.login.MainPresenter;
import com.example.retrofit.ui.login.MainView;
import com.example.retrofit.ui.showprojectlist.ProjectModel;
import com.example.retrofit.utils.BaseApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UploadImageActivity extends BaseActivity implements View.OnClickListener, MainView {

    public static final String TAG = "UploadImageActivity";

    @BindView(R.id.civ_patientprofile)
    CircleImageView civ_patientprofile;
    @BindView(R.id.fb_camera)
    FloatingActionButton fb_camera;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.ed_projectName)
    TextView ed_projectName;

    // =========== Upload image ================
    private static final int GALLERY_IMAGE =104;
    private static  final  int CAMERA_IMAGE =105;
    private static  final  int MY_CAMERA_CODE=102;

    Bitmap bitmap,scaledBitmap;
    String imgFileLocation;
    File imageFile;
    private Uri imageUri;
    String url="",projectName;
    AlertDialog dialog;
    File img;

    Context mContext;

    @Inject
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static String MyPREFERENCES = "Test";

    @Inject
    ApiInterface apiInterface;


    ProgressDialog progressDialog;

    MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);


        mContext=getApplicationContext();
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        sharedPreferences=mContext.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        presenter = new MainPresenter(this,apiInterface);

//        username=sharedPreferences.getString("USERNAME","");


        setChildActivity(true);
    }

    @Override
    protected void injectDependencies(BaseApp baseApp, ApplicationComponent component) {
        component.inject(this);
    }

    @OnClick(R.id.btn_save)
    public  void btn_save(){
        projectName=ed_projectName.getText().toString();

        presenter.uploadImage(projectName,img);
//        presenter.uploadImage(projectName);
//        presenter.uploadImage(img);
    }


    //=================uploadimage=============//

    @OnClick(R.id.fb_camera)
    public void uploadImage(){
        uploadImageOption();
    }

    private void uploadImageOption() {

        AlertDialog.Builder Builder =new AlertDialog.Builder(UploadImageActivity.this);
        Builder.setCancelable(false);
        View view = LayoutInflater.from(UploadImageActivity.this).inflate(R.layout.alert_image_picker_action_dialog,null);
        Builder.setView(view);
        dialog=Builder.create();
        dialog.show();
        ImageView iv_takephoto =view.findViewById(R.id.iv_takephoto);
        ImageView iv_galleryphoto =view.findViewById(R.id.iv_galleryphoto);

        iv_takephoto.setOnClickListener(this);
        iv_galleryphoto.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.iv_takephoto:
                requestPermission(CAMERA_IMAGE);
                dialog.dismiss();
                break;
            case R.id.iv_galleryphoto:
                requestPermission(GALLERY_IMAGE);
                dialog.dismiss();
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_IMAGE){
            if(resultCode==RESULT_OK){
                Uri uri =data.getData();
                final InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(uri);
//                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    loadProfile(uri.toString());
                    //SET HEIGHT AND WIDTH  FOR BITMAP
                    scaledBitmap =getScaledBitmap(bitmap,550,500);

                    new UploadImageActivity.AsyncTaskUploadImage().execute();
                } catch (IOException ex){
                    ex.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        else if (requestCode == CAMERA_IMAGE && resultCode == RESULT_OK) {
            rotateImage(setReducedImageSize());
        }
    }


    private void loadProfile(String url) {

        GlideApp.with(mContext).load(url)
                .into(civ_patientprofile);
        civ_patientprofile.setColorFilter(ContextCompat.getColor(mContext, android.R.color.transparent));
    }

    private void requestPermission(int requestCode) {
        if(requestCode==GALLERY_IMAGE){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);

            } else {
                selectFromGallery();
            }
        }else if (requestCode==CAMERA_IMAGE){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode);

            } else {
                captureImageUsingCamera();
            }

        }
    }


    private void captureImageUsingCamera() {
        Intent takePictureIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File image;
        try{
            image=createImageFile();
            Log.d(TAG, "captureImageUsingCamera: "+image);
            if(image!=null){
                imageUri= FileProvider.getUriForFile(this,"com.example.retrofit.fileprovider",image);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if(takePictureIntent.resolveActivity(this.getPackageManager())!=null){
                    startActivityForResult(takePictureIntent,CAMERA_IMAGE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void selectFromGallery() {
        Intent galleryIntent =new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_IMAGE);
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface=null;
        try{
            exifInterface=new ExifInterface(imgFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation =exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix=new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:


        }
        scaledBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        new UploadImageActivity.AsyncTaskUploadImage().execute();
    }

    private Bitmap setReducedImageSize() {
        int targetImageViewWidth=500;
        int targetImageHeight=500;

        BitmapFactory.Options bmOptions =new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imgFileLocation,bmOptions);

        int CameraImageWidth=bmOptions.outWidth;
        int CameraImageHeight=bmOptions.outHeight;

        int scaleFactor=Math.min(CameraImageWidth/targetImageViewWidth,CameraImageHeight/targetImageHeight);
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inJustDecodeBounds=false;

        return BitmapFactory.decodeFile(imgFileLocation,bmOptions);
    }

    private Bitmap getScaledBitmap(Bitmap bitmap, int width, int height) {
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        int nWidth = bWidth;
        int nHeight = bHeight;

        if(nWidth > width) {
            int ratio = bWidth / width;
            if(ratio > 0)
            {
                nWidth = width;
                nHeight = bHeight / ratio;
            }
        }

        if(nHeight > height) {
            int ratio = bHeight / height;
            if(ratio > 0) {
                nHeight = height;
                nWidth = bWidth / ratio;
            }
        }

        return Bitmap.createScaledBitmap(bitmap, nWidth, nHeight, true);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = sharedPreferences.getString("USERNAME","").isEmpty()?"DRAPP":sharedPreferences.getString("USERNAME","")+"_"+timeStamp;
        File storageDir =this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(timeStamp,".jpg",storageDir);
        imgFileLocation=image.getAbsolutePath();
        return  image;
    }




//there is problem of sharedPreference*************

// Upload Sign image online on server function.

    class  AsyncTaskUploadImage extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog at image upload time.
//            showProgressBar();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Dismiss the progress dialog after done uploading.
//            hideProgressBar();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                convertImageBitmap(scaledBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private void setProfileImage(Bitmap scaledBitmap) {
        civ_patientprofile.setImageBitmap(scaledBitmap);

    }

    private void convertImageBitmap(Bitmap selectedImage) throws IOException {


         img = createImageFile();
        OutputStream os;
        try {
            os = new FileOutputStream(img);
//            selectedImage.compress(Bitmap.CompressFormat.WEBP, 60, os);
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();

        } catch (Exception e) {

        }
        Log.d(TAG, "convertImageBitmap: "+img);
        uploadGalleryImage(img);
//        setProfileImage(scaledBitmap);
    }

    private void uploadGalleryImage(File imageFile) {
//        presenter.uploadImage(imageFile);// ************imageApi
    }
//=================end==================//


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
    public void getProjectList(List<ProjectModel> projectModelList) {

    }

    @Override
    public void onFormUploded(List<ImageModel> imageModelList) {
        for(ImageModel imageModel:imageModelList){
            int status=imageModel.getStatus();
            if(status==200){
//                setProfileImage(scaledBitmap);
                Toast.makeText(UploadImageActivity.this, "Upload Image Successfully !!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(UploadImageActivity.this, "Error..!!", Toast.LENGTH_SHORT).show();
            }

        }

    }


}
