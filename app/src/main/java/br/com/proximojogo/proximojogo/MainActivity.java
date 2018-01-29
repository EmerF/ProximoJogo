package br.com.proximojogo.proximojogo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;

import br.com.proximojogo.proximojogo.ui.AgendaFragment;
import br.com.proximojogo.proximojogo.ui.ArenaFragment;
import br.com.proximojogo.proximojogo.ui.CriarBannerConfrontoFragment;
import br.com.proximojogo.proximojogo.ui.GoogleAuthFragment;
import br.com.proximojogo.proximojogo.ui.ListaEventosAgenda;
import br.com.proximojogo.proximojogo.ui.ListaEventosPassadosAgenda;
import br.com.proximojogo.proximojogo.ui.TimeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RESULT_SELECT_IMG = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final String TAG = "LEITURA_IMAGEM";
    private ImageView ivAvatar;
    private Bitmap bitmap;
    private Uri mCropImageUri;
    private String avatarProximoJogo = "avatar_proximo_jogo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestStoragePermission();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new GoogleAuthFragment()).commit();
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.container);
//
//        if(fragmentById == null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
//        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        ivAvatar = (ImageView) hView.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando"), IMAGE_REQUEST_CODE);
                selectImageFromGallary();
            }
        });

        leituraAvatar();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();

        } else if (id == R.id.drawer_cadastrar_time) {
            fragmentManager.beginTransaction().replace(R.id.container, new TimeFragment()).commit();
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.container, new CriarBannerConfrontoFragment()).commit();
        }else if(id == R.id.drawer_cadastrar_arena){

            fragmentManager.beginTransaction().replace(R.id.container, new ArenaFragment()).commit();
        } else if (id == R.id.jogos_passados) {
            fragmentManager.beginTransaction().replace(R.id.container, new ListaEventosPassadosAgenda()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        .setMaxCropResultSize(80, 80)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(this);
            }
            // when image is cropped
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("APP_DEBUG", result.toString());
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Log.d("APP_DEBUG", resultUri.toString());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ivAvatar.setImageBitmap(bitmap);
                    String content = "avatarPJ";

                    File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Camera");

                    File imageFile = new File(storageDir, avatarProximoJogo);
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(imageFile);

                        int quality = 100;
                        bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else {
                Toast.makeText(this, "Nenhuma imagem selecionada!",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //aqui explica pq vc precisa da permissao
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void selectImageFromGallary() {
        Intent intent = CropImage.activity(mCropImageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMinCropResultSize(500, 500)
                .setMaxCropResultSize(1000, 1000)
                .getIntent(this);
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


    public void leituraAvatar() {
        File imgFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera/" + avatarProximoJogo);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivAvatar.setImageBitmap(myBitmap);

        }

    }
}
