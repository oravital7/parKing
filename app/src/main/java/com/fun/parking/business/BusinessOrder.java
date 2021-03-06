package com.fun.parking.business;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.fun.parking.BaseActivity;
import com.fun.parking.MainActivity;
import com.fun.parking.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class BusinessOrder extends BaseActivity {

    private Button takePhoto;
    private ImageView showPhoto;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private String currentPhotoPath;
    private Uri photoURI;
    private String parkingId;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_order);
        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        parkingId = getIntent().getStringExtra("parkingId");
        mProgress = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        System.out.println(mStorage);
        takePhoto = (Button) findViewById(R.id.takePicOfParking);
        showPhoto = (ImageView) findViewById(R.id.parkingPhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkPermission(Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE);



            }
        });
        String start = getIntent().getStringExtra("startDate");
        String end = getIntent().getStringExtra("endDate");
        final TextView info1 = (TextView) findViewById(R.id.info1);
        final TextView info3 = (TextView) findViewById(R.id.info3);
        info1.setText("from: "+start+","+getIntent().getStringExtra("startTime"));
        info3.setText("to: "+end+","+getIntent().getStringExtra("endTime"));


    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(BusinessOrder.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(BusinessOrder.this,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(BusinessOrder.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
            dispatchTakePictureIntent();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading Image...");
            mProgress.show();
            Uri uri = photoURI;
            final FirebaseAuth fAuth = FirebaseAuth.getInstance();
            String userId = fAuth.getCurrentUser().getUid();
            StorageReference filepath = mStorage.child("parkingPhotos").child(parkingId);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();
                    //show photo on the screen
                    taskSnapshot
                            .getStorage()
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    String download_url = o.toString();
                                    Picasso.with(BusinessOrder.this).load(download_url).fit().centerCrop().into(showPhoto);

                                }

                            });

                 

                }
            })
            ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BusinessOrder.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                dispatchTakePictureIntent();

            } else {
                Toast.makeText(BusinessOrder.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }
}
