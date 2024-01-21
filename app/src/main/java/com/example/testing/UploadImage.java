package com.example.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentResolver;

import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.storage.OnProgressListener;

public class UploadImage extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri cameraImageUri;
    private ActivityResultLauncher<Intent> cameraLauncher;

    private FloatingActionButton uploadButton, backButton,cameraButton;
    ImageView imgCamera;
    private ImageView uploadImage;
    EditText uploadCaption;
    ProgressBar progressBar;
    ImageButton addressBtn;
    private Uri imageUri;
    FirebaseAuth auth;
    FirebaseUser user;
    CheckBox checkBox;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address, textUser, diachi;
    String userName, email, nameUser;
    Button getLocation;
    boolean checkState = false;

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
    final Date now = new Date();
    final String fileName = formatter.format(now);
    final String date = "Ngày ";

    Date currentDate = new Date();
    // Định dạng ngày theo định dạng "yyyyMMdd"
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/", Locale.getDefault());
    // Gắn biến ngày vào chuỗi
    String dateString = sdf.format(currentDate);

    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/YY", Locale.getDefault());
    // Gắn biến ngày vào chuỗi
    String fmtString = fmt.format(currentDate);
    String d = "Đã điểm danh ngày " + fmtString;
    boolean status = true;

    public void onBackPressed() {
        // Quay về màn hình trước đó
        Intent intent = new Intent(this, Select.class);
        startActivity(intent);
        finish(); // Kết thúc hiện tại Activity để ngăn chặn người dùng quay lại nó bằng nút back
    }


    private final static int REQUEST_CODE = 100;

    final private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    final private DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("attendace").child(fileName);
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference(date + ' ' + dateString);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        address = findViewById(R.id.address);
        addressBtn = findViewById(R.id.addressBtn);
        auth = FirebaseAuth.getInstance();
        backButton = findViewById(R.id.backButton);

//        diachi = findViewById(R.id.diachi);

        getLocation = findViewById(R.id.getLocation);
        textUser = findViewById(R.id.textUser);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        uploadButton = findViewById(R.id.uploadButton);
        uploadCaption = findViewById(R.id.uploadCaption);
        uploadImage = findViewById(R.id.uploadImage);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        user = auth.getCurrentUser();
        String userId = user.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);
                if (student != null) {
                    String studentName = student.getStudentName();
                    textUser.setText(studentName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UploadImage.this, "fail", Toast.LENGTH_LONG).show();
            }
        });

        //lay vi tri
        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBtn.setVisibility(View.GONE);
                getLastLocation();

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadImage.this, StudentDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(UploadImage.this)
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
//                    uploadToFirebase(imageUri);
                    uploadImageToFirebase(imageUri);
                } else {
                    Toast.makeText(UploadImage.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(UploadImage.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    //lattitude.setText("Lattitude: "+addresses.get(0).getLatitude());
                                    //longitude.setText("Longitude: "+addresses.get(0).getLongitude());
//                                    diachi.setText("Longitude: "+addresses.get(0).getLongitude());
                                    address.setText("Vị trí hiện tại: " + addresses.get(0).getAddressLine(0));
                                    //city.setText("City: "+addresses.get(0).getLocality());
                                    //country.setText("Country: "+addresses.get(0).getCountryName());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(UploadImage.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(UploadImage.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Check if data is not null and resultCode is RESULT_OK
    if (data != null && resultCode == RESULT_OK) {
        // Get the selected image URI
        imageUri = data.getData();

        // Check if the URI is not null before setting it to the ImageView
        if (imageUri != null) {
            uploadImage.setImageURI(imageUri);
        }
    }
}
private String getCurrentTimestamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    return sdf.format(new Date());
}
private void uploadImageToFirebase(Uri imageUri) {

        String caption = uploadCaption.getText().toString();
        String location = address.getText().toString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = user.getEmail();

    if (imageUri != null) {
        // Create a reference to "images/<FILENAME>.jpg"
        StorageReference imageRef = storageReference.child(uid + "." + getFileExtension(imageUri));

        // Upload the file to Firebase Storage
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Register observers to listen for when the upload is successful or fails
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DataClass dataClass = new DataClass(uri.toString(), caption, location);
                        String key = databaseReference.push().getKey();
                        String caption1 = uploadCaption.getText().toString();

                        if (user == null) {
                            Intent intent = new Intent(getApplicationContext(), Select.class);
                            startActivity(intent);
                            finish();
                        } else {
                            userName = user.getUid().toString();
                        }


//                        user = auth.getCurrentUser();
                        String userId = user.getUid();
                        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Student student = snapshot.getValue(Student.class);
                                if (student != null) {
                                    String studentName = student.getStudentName();
                                    databaseReference1.child(userName).child("studentName").setValue(studentName);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(UploadImage.this, "fail", Toast.LENGTH_LONG).show();
                            }
                        });
                        databaseReference.child(userName).child("status").setValue(fileName);
                        databaseReference1.child(userName).setValue(dataClass);
                        databaseReference1.child(userName).child("diachi").setValue(address.getText().toString());
//                        databaseReference1.child(userName).child(dateString).child("checkStatus").setValue("da diem danh");
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadImage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadImage.this, Select.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadImage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
}
