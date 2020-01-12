package com.fun.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.w3c.dom.Document;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

//public class UserProfile extends AppCompatActivity {
public class UserProfile extends BaseActivity{
    TextView fullName,email,phone,address, changeImg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        changeImg = findViewById(R.id.changeProfilePic);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        address = findViewById(R.id.address);
        logout = findViewById(R.id.logout);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);

        userId = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("users").document(userId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists())
                {
                    phone.setText(document.getString("phone"));
                    fullName.setText(document.getString("fName"));
                    email.setText(document.getString("email"));
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                FirebaseAuth.getInstance().signOut(); // logout
                finish();
            }
        });

//        changeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
//                // Sets the type as image/*. This ensures only components of type image are selected
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "pick an image"), requestCode 1);
//            }
//        });


    }
//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data)
//    {
//        // Result code is RESULT_OK only if the user selects an Image
//        if (resultCode == RESULT_OK&&requestCode==1)
//            switch (requestCode){
//                case GALLERY_REQUEST_CODE:
//                    //data.getData returns the content URI for the selected Image
//                    Uri selectedImage = data.getData();
//                    imageView.setImageURI(selectedImage);
//                    break;
//            }
//    }
}



