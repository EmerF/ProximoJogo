package br.com.proximojogo.proximojogo.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import br.com.proximojogo.proximojogo.R;

/**
 * @author Ale
 */
public class CriarBannerConfrontoFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_REQUEST_CODE = 203;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int RESULT_SELECT_IMG = 3;
    private ImageView imageView;
    private ImageView imageView2;
    private Button btnUpload;
    private Bitmap bitmap;
    private Uri filePath;
    private Activity activity;
    private boolean imagemUm;
    private boolean imagemDois;
    private Uri mCropImageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_criar_banner_confronto, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
//        btnUpload = (Button) view.findViewById(R.id.btn_upload);

        requestStoragePermission();

        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
//        btnUpload.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == imageView) {
            imagemUm = true;
            imagemDois = false;
            selectImageFromGallary();
//            CropImage.startPickImageActivity(activity);


//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Complete a ação usando"), IMAGE_REQUEST_CODE);
        }
        if (v == imageView2) {
            imagemUm = false;
            imagemDois = true;
            selectImageFromGallary();
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Complete a ação usando"), IMAGE_REQUEST_CODE);
        } else if (v == btnUpload) {
            //enviar para servidor
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            activity = (Activity) context;
        }

    }

    //novo jeito de crop
    private void selectImageFromGallary() {
        Intent intent = CropImage.activity(mCropImageUri)
                .getIntent(getContext());
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete a ação usando"), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        // Create intent to Open Image
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, RESULT_SELECT_IMG);
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_SELECT_IMG);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("APP_DEBUG", String.valueOf(requestCode));

        try {
            // When an Image is picked
            if (requestCode == RESULT_SELECT_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();

                CropImage.activity(selectedImage)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(80, 80)
                        .setMaxCropResultSize(800, 800)
                        .start(getContext(), this);
            }
            // when image is cropped
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("APP_DEBUG", result.toString());
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Log.d("APP_DEBUG", resultUri.toString());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
//                    profilePic.setImageBitmap(bitmap);
                    if (imagemUm)
                        imageView.setImageBitmap(bitmap);
                    else
                        imageView2.setImageBitmap(bitmap);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
 /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
//                startCropImageActivity(imageUri);

                if (data != null && data.getData() != null) {
                    filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);
                        if (imagemUm) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView2.setImageBitmap(bitmap);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
//        if (requestCode == IMAGE_REQUEST_CODE && resultCode == activity.RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);
//                if(imagemUm){
//                    imageView.setImageBitmap(bitmap);
//                }else{
//                    imageView2.setImageBitmap(bitmap);
//
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }*/

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //aqui explica pq vc precisa da permissao
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(activity);
            } else {
                Toast.makeText(activity, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(activity, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getActivity(), "Precisa de permissao para acessar a imagem", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getActivity(), "Oops você não tem permissao", Toast.LENGTH_LONG).show();
//
//            }
//        }

    }

    //inicio crop
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(getContext(), this);
    }

    public void onSelectImageClick(View view) {
        if (CropImage.isExplicitCameraPermissionRequired(activity)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
        } else {
            CropImage.startPickImageActivity(activity);
        }
    }
}
