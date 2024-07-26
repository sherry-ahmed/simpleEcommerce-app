package com.example.shoppingapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class NewProductActivity extends AppCompatActivity {
    TextView tvHeadline, tvDescription, tvPrice, tvProductType, tvBrand, tvGender;
    ImageView ivUpload, ivProductImage;
    Button btSubmit;
    Uri Imageuri;
    RelativeLayout relative;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bar = new ProgressBar(this);

        tvHeadline = findViewById(R.id.tvHeadline);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvProductType = findViewById(R.id.tvProductType);
        tvBrand = findViewById(R.id.tvBrand);
        tvGender = findViewById(R.id.tvGender);
        ivUpload = findViewById(R.id.ivUpload);
        ivProductImage = findViewById(R.id.ivProductImage);
        btSubmit = findViewById(R.id.btSubmit);
        relative = findViewById(R.id.relative);

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
                relative.setVisibility(View.VISIBLE);
                ivUpload.setVisibility(View.GONE);
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors
                tvHeadline.setError(null);
                tvDescription.setError(null);
                tvPrice.setError(null);
                tvProductType.setError(null);
                tvBrand.setError(null);
                tvGender.setError(null);

                // Get text from fields
                String headline = tvHeadline.getText().toString().trim();
                String description = tvDescription.getText().toString().trim();
                String price = tvPrice.getText().toString().trim();
                String productType = tvProductType.getText().toString().trim();
                String brand = tvBrand.getText().toString().trim();
                String gender = tvGender.getText().toString().trim();

                // Check for empty fields
                if (headline.isEmpty()) {
                    tvHeadline.setError("Headline is required");
                    tvHeadline.requestFocus();
                    return;
                }
                if (description.isEmpty()) {
                    tvDescription.setError("Description is required");
                    tvDescription.requestFocus();
                    return;
                }
                if (price.isEmpty()) {
                    tvPrice.setError("Price is required");
                    tvPrice.requestFocus();
                    return;
                }
                if (productType.isEmpty()) {
                    tvProductType.setError("Product Type is required");
                    tvProductType.requestFocus();
                    return;
                }
                if (brand.isEmpty()) {
                    tvBrand.setError("Brand is required");
                    tvBrand.requestFocus();
                    return;
                }
                if (gender.isEmpty()) {
                    tvGender.setError("Gender is required");
                    tvGender.requestFocus();
                    return;
                }
                if (Imageuri == null) {
                    Toast.makeText(NewProductActivity.this, "Product image is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                btSubmit.setEnabled(false); // Disable the button to prevent multiple clicks

                final StorageReference reference = FirebaseStorage.getInstance().getReference()
                        .child("product_images/" + System.currentTimeMillis());
                reference.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String key = FirebaseDatabase.getInstance().getReference().child("product").push().getKey();
                                ProjectModel model = new ProjectModel(tvHeadline.getText().toString().trim(),
                                        tvDescription.getText().toString().trim(),
                                        tvPrice.getText().toString().trim(),
                                        tvProductType.getText().toString().trim(),
                                        tvBrand.getText().toString().trim(),
                                        tvGender.getText().toString().trim(),
                                        uri.toString(), key);

                                FirebaseDatabase.getInstance().getReference().child("product").child(key)
                                        .setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(NewProductActivity.this, "Product Uploaded", Toast.LENGTH_SHORT).show();
                                                btSubmit.setEnabled(true);
                                                finish(); // Close the activity after successful upload
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(NewProductActivity.this, "Failed to upload product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                btSubmit.setEnabled(true);
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewProductActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        btSubmit.setEnabled(true);
                    }
                });
            }
        });
    }

    private void UploadImage() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(NewProductActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            Imageuri = data.getData();
            ivProductImage.setImageURI(Imageuri);



        }
    }
}