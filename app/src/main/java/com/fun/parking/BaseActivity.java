package com.fun.parking;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fun.parking.customer.HistoryOrdersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {

        fAuth = FirebaseAuth.getInstance();

        final DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_activity, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("parKing");
        FirebaseFirestore fStore;
        fStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
        String userId;
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        final TextView name=(TextView)headerView.findViewById(R.id.nameProfile);
        final TextView adr=(TextView)headerView.findViewById(R.id.AddressProfile);
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists())
                {
                   //set the header with the user's details.
                    String address=document.getString("address.street")+" ";
                    address+=document.getString("address.houseNumber")+", ";
                    address+=document.getString("address.city");
                    adr.setText(address);
                    name.setText(document.getString("fName"));

                }

            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        Intent intent = null;
                        switch (menuItem.getItemId()) {

                            case R.id.nav_profile:
                                 intent = new Intent(getBaseContext(), UserProfile.class);
                                break;

                            case R.id.Contact_us:
                                 intent = new Intent(getBaseContext(), ContactForm.class);
                                break;

                            case R.id.orderHistory:
                                intent = new Intent(getBaseContext(), HistoryOrdersActivity.class);
                                break;

                            case  R.id.Logout:
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                FirebaseAuth.getInstance().signOut(); // logout
                                break;
                            case  R.id.Home:
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                break;
                        }

                        if (intent != null)
                        {
                            menuItem.setChecked(true);
                            startActivity(intent);
                            fullView.closeDrawer(GravityCompat.START);
                        }

                        return true;
                    }
                });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("profilePhotos/").child(fAuth.getUid());
        final CircleImageView img=headerView.findViewById(R.id.profile_image_menu);
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    if (task.getResult() != null) {
                        Glide.with(BaseActivity.this)
                                .load(task.getResult())
                                .placeholder(R.drawable.profile)
                                .apply(RequestOptions.circleCropTransform())
                                .into(img);
                    }
                }
            }
        });
    }

    }

