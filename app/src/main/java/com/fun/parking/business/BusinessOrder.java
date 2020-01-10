package com.fun.parking.business;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fun.parking.BaseActivity;
import com.fun.parking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class BusinessOrder extends BaseActivity {
 private Button takePhoto;
 private ImageView showPhoto;
 private static final int CAMERA_REQUEST_CODE=1;
 private StorageReference mStorage;
 private ProgressDialog mProgress;
protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.business_order);
    mProgress=new ProgressDialog(this);
    mStorage= FirebaseStorage.getInstance().getReference();
    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
    System.out.println(mStorage);
    takePhoto=(Button)findViewById(R.id.takePicOfParking);
    showPhoto=(ImageView)findViewById(R.id.parkingPhoto);
    takePhoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("photo",MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
            startActivityForResult(intent,CAMERA_REQUEST_CODE);


        }
    });
    String start=getIntent().getStringExtra("startDate");
    String end=getIntent().getStringExtra("endDate");
    final TextView info=(TextView)findViewById(R.id.info1);
    final TextView info1=(TextView)findViewById(R.id.info2);
    final TextView info3=(TextView)findViewById(R.id.info3);
    String good="You offer your parking spot between:\n";
    info.setText(start);
    info1.setText(good);
    info3.setText(end);

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        System.out.println("data is :"+data.getData());
        if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK&& data!=null){

            mProgress.setMessage("Uploading Image...");
            mProgress.show();

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

           // Uri uri=data.getData();

         //  Uri uri= data.getData();
            Bundle bundle = data.getExtras();
            Uri uri = (Uri)bundle.get(bundle.getString("photo"));
            final FirebaseAuth fAuth = FirebaseAuth.getInstance();
            String userId=fAuth.getCurrentUser().getUid();
            StorageReference filepath=mStorage.child("parkingPhotos").child( "aa");
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                 mProgress.dismiss();
                    Toast.makeText(BusinessOrder.this,"Uploading finished...",Toast.LENGTH_LONG).show();

                }
            })
            ;
        }
    }

}
