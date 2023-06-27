package com.mine.app.Products;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mine.app.Lists.Brands;
import com.mine.app.Lists.Categories;
import com.mine.app.Models.ProductModel;
import com.mine.app.R;
import com.mine.app.Utility.NetworkChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddProduct extends AppCompatActivity {
    //listener to wifi status
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private static final int PICK_IMAGE_REQUEST = 123;
    AppCompatButton add_btn; //add btn
    ImageView product_image; // image
    ImageButton upload_image_btn; //upload button
    TextView
            brand, //brand
            categoryTv;//category
    EditText product_name_et //name
            , price_et, //price
            color_et, //color
            stock_et,
            product_number_Spinner,//quantity
            description_tv; //description

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //image picked uri
    private Uri image_uri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private AppCompatButton cameraBtn, galleryBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);



                initView();
                firebaseAuth = FirebaseAuth.getInstance();
                //setup progressDialog
               progressDialog = new ProgressDialog(this);
               progressDialog.setTitle("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);

                //init permission arrays
                cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                storagePermissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
                add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                product_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImagePickDialog();
                    }
                });

                categoryTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryDialog();
                    }
                });
        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandDialog();
            }
        });

                add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Flow
                        //input data
                        //validate data
                        inputData();
                    }
                });

            }


            private void uploadImageToFirebaseStorage(Uri imageUri) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());
                imagesRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Image uploaded successfully
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Image upload failed
                            }
                        });
            }




            private void initView(){
                //init views
                add_btn = findViewById(R.id.add_btn);

                product_image = findViewById(R.id.product_image);
                //upload_image_btn = findViewById(R.id.upload_image_btn);

                product_name_et = findViewById(R.id.product_name_et);
                price_et = findViewById(R.id.price_et);
                description_tv = findViewById(R.id.description_tv);
                categoryTv = findViewById(R.id.product_category_Spinner);
                product_number_Spinner = findViewById(R.id.product_number_Spinner);

                color_et = findViewById(R.id.color_et);
                brand = findViewById(R.id.brand);

            }
            private String productTitle, productDescription, productCategory,productQuantity, originalPrice, productColor, productBrand ;
            //private boolean discountAvailable = false;
            private void inputData(){
                //input data
                productTitle = product_name_et.getText().toString().trim();

                productCategory = categoryTv.getText().toString().trim();
                productBrand = brand.getText().toString().trim();
                productQuantity = product_number_Spinner.getText().toString().trim();
                originalPrice = price_et.getText().toString().trim();

                productColor = color_et.getText().toString().trim();
                productDescription = description_tv.getText().toString().trim();
                //Validate data
                if (TextUtils.isEmpty(productTitle)) {
                    Toast.makeText(this, "Title is required.", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(productCategory)) {
                    Toast.makeText(this, "Category is required.", Toast.LENGTH_SHORT).show();
                }
                /*****if (TextUtils.isEmpty(productBrand)) {
                 Toast.makeText(this, "Brand is required.", Toast.LENGTH_SHORT).show();
                 }

                 if (TextUtils.isEmpty(productQuantity)) {
                 Toast.makeText(this, "Quantity is required.", Toast.LENGTH_SHORT).show();
                 }*/

                if (TextUtils.isEmpty(originalPrice)) {
                    Toast.makeText(this, "Price is required.", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(productColor)) {
                    Toast.makeText(this, "Color is required.", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(productDescription)) {
                    Toast.makeText(this, "Description is required.", Toast.LENGTH_SHORT).show();
                }
                addProduct();





            }
            private void addProduct(){
                progressDialog.setMessage("adding Product");



                progressDialog.show();
                Log.d("TAG00000000000000000000001", "Message to log"+image_uri);
                final String timestamp = ""+System.currentTimeMillis();
                if(image_uri == null){

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("productId", ""+timestamp);
                    hashMap.put("productTitle",""+productTitle);
                    hashMap.put("productCategory",""+productCategory);
                    hashMap.put("productBrand",""+productBrand);

                    hashMap.put("productQuantity",""+productQuantity);
                    hashMap.put("originalPrice",""+originalPrice);
                    hashMap.put("productColor",""+productColor);
                    hashMap.put("productDescription",""+productDescription);
                    hashMap.put("productIcon", "");//no image
                    hashMap.put("timestamp",""+timestamp);
                    hashMap.put("uid",""+firebaseAuth.getUid());
                    //add to db
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Log.d("TAG00000000000000000000001", "Message to log"+reference);
                    reference.child("Products").child(timestamp).setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>(){
                                @Override
                                public void onSuccess(Void aVoid){
                                    //added to db
                                    progressDialog.dismiss();
                                    Toast.makeText(AddProduct.this, "Product added", Toast.LENGTH_SHORT).show();
                                    clearData();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e){
                                    progressDialog.dismiss();
                                    Toast.makeText(AddProduct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }else{
                    //upload with image
                    //first upload image to storage
                    //name and path of image to be uploaded
                    String filePathAndName = "Product_images/"+""+timestamp;
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("product_images").child(System.currentTimeMillis() + ".jpg");

                    Log.d("TAG01111111111111111", "add img " + storageReference);
                    storageReference.putFile(image_uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                                    Log.d("TAG01111111111111111", "Email from login: " + storageReference);
                                    //image uploaded
                                    //get url of uploaded image
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isSuccessful());
                                    Uri downloadImageUri = uriTask.getResult();
                                    if(uriTask.isSuccessful()) {

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("productId", "" + timestamp);
                                        hashMap.put("productTitle", "" + productTitle);
                                        hashMap.put("productCategory", "" + productCategory);
                                        hashMap.put("productBrand", "" + productBrand);

                                        hashMap.put("productQuantity", "" + productQuantity);
                                        hashMap.put("originalPrice", "" + originalPrice);
                                        hashMap.put("productColor", "" + productColor);
                                        hashMap.put("productDescription", "" + productDescription);
                                        hashMap.put("productIcon", "" + downloadImageUri);
                                        hashMap.put("timestamp", "" + timestamp);
                                        hashMap.put("uid", "" + firebaseAuth.getUid());
                                        //add to db
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        reference.child("Products").child(timestamp).setValue(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //added to db
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddProduct.this, "Product added", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(AddProduct.this, RecyclerView.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddProduct.this, "ereur here" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e){
                                    Log.d("TAG", "Email from login: " + storageReference);
                                    //failed uploading image
                                    progressDialog.dismiss();
                                    Toast.makeText(AddProduct.this, "no here"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                }

            }
            private void clearData(){
                //
                product_number_Spinner.setText("");//quantity
                brand.setText("");//brand
                categoryTv.setText("");//category
                product_name_et.setText(""); //name
                price_et.setText(""); //price
                color_et.setText("");//color
                stock_et.setText("");
                description_tv.setText("");
                image_uri= null;//description

            }
            private void categoryDialog(){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Product Category")
                        .setItems(Categories.productCategories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get picked category
                                String category = Categories.productCategories[which];
                                //set picked category
                                categoryTv.setText(category);
                            }
                        }).show();
            }
    private void brandDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Brand")
                .setItems(Brands.brands, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String brandd = Brands.brands[which];
                        //set picked category
                        brand.setText(brandd);
                    }
                }).show();
    }

            private void showImagePickDialog() {
                //options to display in dialog
                String[] options = {"Gallery"};
                //dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pick Image")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //handle item clicks
                                if(which==0){
                                    //gallery clicked
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                                }/*else{
                                    //gallery clicked
                                    if(checkStoragePermission()){
                                        //permission ganted
                                        pickFromGallery();
                                    }else{
                                        //permission not granted , request
                                        requestStoragePermission();
                                    }
                                }*/
                            }
                        }).show();
            }
            private void pickFromGallery(){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("/image/*");
                startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
            }
            private void pickFromCamera(){
                //intent to pick image from gallery

                //using media store to pick high/orignal quality image
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
                contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

                image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
                startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

            }
            private boolean checkStoragePermission(){
                boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        (PackageManager.PERMISSION_GRANTED);
                return result;
            }
            private void requestStoragePermission(){
                ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
            }
            private boolean checkCameraPermission(){
                boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                        (PackageManager.PERMISSION_GRANTED);
                boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        (PackageManager.PERMISSION_GRANTED);
                return result && result1;
            }
            private void requestCameraPermission(){
                ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
            }

            //handle permission results
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
                switch (requestCode){
                    case CAMERA_REQUEST_CODE:{
                        if(grantResults.length>0){
                            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                            boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                            if(cameraAccepted && storageAccepted){
                                //both permissions granted
                                pickFromCamera();
                            }else{
                                //both or one of permissions denied
                                Toast.makeText(this, "Camera & Storage permissions are required.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }case STORAGE_REQUEST_CODE:{
                        if(grantResults.length>0){
                            boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                            if(storageAccepted){
                                //both permissions granted
                                pickFromGallery();
                            }else{
                                //both or one of permissions denied
                                Toast.makeText(this, "Storage permission is required.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            //handle image pick results
            @Override
            protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    image_uri = data.getData();
                    try {
                        product_image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                        //image picked from camera
                        product_image.setImageURI(image_uri);

                    }
                }
    @Override
    protected void onStart(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }
    @Override
    protected void onStop(){
        unregisterReceiver(networkChangeListener);
        super.onStop();

    }


        }
