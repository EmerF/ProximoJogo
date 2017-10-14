package br.com.proximojogo.proximojogo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.proximojogo.proximojogo.BuildConfig;
import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;

/**
 * @author Ale
 */
public class CriarBannerConfrontoFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_REQUEST_CODE = 203;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int RESULT_SELECT_IMG = 3;
    private ImageView imageView2;
    private ImageView imageView;
    private Button btnUpload;
    private Bitmap bitmap;
    private Uri filePath;
    private Activity activity;
    private boolean imagemUm;
    private boolean imagemDois;
    private Uri mCropImageUri;
    private Button btnShare;
    private ShareActionProvider mShareActionProvider;
    private View mCurrentUrlMask;
    private File imageFile;
    String mCurrentPhotoPath;
    // data object we want to retain
    private CriarBannerConfrontoFragment data;


    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_criar_banner_confronto, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Criar Banner de Confronto");
        Button novaAgenda = (Button) view.findViewById(R.id.btn_share);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                shareImage(imageFile);

            }
        });

        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
//        btnUpload = (Button) view.findViewById(R.id.btn_upload);

        requestStoragePermission();

        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        return view;
    }

    private void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        View v1 = getActivity().getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        File imageFile = new File(storageDir, imageFileName);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);

            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        this.imageFile = imageFile;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openScreenshot(File imageFile) {
        try {
            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_SEND);
//            Uri uri = Uri.fromFile(imageFile);
            Uri uri = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    this.imageFile);
            intent.setDataAndType(uri, "image/*");
            startActivity(intent);
        } catch (FileUriExposedException e) {
            e.printStackTrace();
        }
    }
    private void shareImage(File file){
//        Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                this.imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageView) {
            imagemUm = true;
            imagemDois = false;
            selectImageFromGallary();
        }
        if (v == imageView2) {
            imagemUm = false;
            imagemDois = true;
            selectImageFromGallary();
        } else if (v == btnShare) {
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
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMinCropResultSize(500, 500)
                .setMaxCropResultSize(1000, 1000)
                .getIntent(getContext());
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete a ação usando"), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
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
                        .setMaxCropResultSize(400, 400)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(getContext(), this);
            }
            // when image is cropped
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
                Toast.makeText(getActivity(), "Nenhuma imagem selecionada!",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

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


    public CriarBannerConfrontoFragment getData() {
        return data;
    }

    public void setData(CriarBannerConfrontoFragment data) {
        this.data = data;
    }

    private void requestStoragePermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            //aqui explica pq vc precisa da permissao
        }
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Precisa de permissao para acessar a imagem",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"Oops você não tem permissao",Toast.LENGTH_LONG).show();

            }
        }

    }
}
