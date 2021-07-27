package com.example.givelify;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givelify.Models.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {

    private Button uploadBtn;
    private ImageView imageView;
    private ProgressBar progBar;
    private Uri imageUri;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Images");
    private StorageReference reference =FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_update);

        uploadBtn = findViewById(R.id.btn_upload);
        imageView = findViewById(R.id.add_photo);
        progBar = findViewById(R.id.progressBar2);
        progBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(l-> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,2);
        });
            uploadBtn.setOnClickListener(v -> {
                if(imageUri !=null){
                    uploadToFirebase(imageUri);
                }
                else {
                    Toast.makeText(ProfileActivity.this,"Please Select an Image",Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void uploadToFirebase(Uri uri) {
        StorageReference fileref = reference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileref.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            fileref.getDownloadUrl().addOnSuccessListener(uri1 -> {
                Image image = new Image(uri1.toString());
                String imageId = root.push().getKey();
                root.child(imageId).setValue(image);
                progBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
            });
        }).addOnProgressListener(snapshot -> progBar.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {
            progBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==2 && resultCode == RESULT_OK && data != null){
        imageUri=data.getData();
        imageView.setImageURI(imageUri);
        }

    }
}
