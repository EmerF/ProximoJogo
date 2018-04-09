package br.com.proximojogo.proximojogo;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileOutputStream;
import br.com.proximojogo.proximojogo.helper.HelperJogador;
import br.com.proximojogo.proximojogo.ui.AgendaFragment;
import br.com.proximojogo.proximojogo.ui.ArenaFragment;
import br.com.proximojogo.proximojogo.ui.CriarBannerConfrontoFragment;
import br.com.proximojogo.proximojogo.ui.GoogleAuthFragment;
import br.com.proximojogo.proximojogo.ui.ListaEventosAgenda;
import br.com.proximojogo.proximojogo.ui.ListaEventosPassadosAgenda;
import br.com.proximojogo.proximojogo.ui.ListaJogadoresTime;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks {
    private static final int RESULT_SELECT_IMG = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int PHONE_NUMBER_PERMISSION_CODE = 132;
    private static final String TAG = "LEITURA_IMAGEM";
    private ImageView ivAvatar;
    private Bitmap bitmap;
    private Uri mCropImageUri;
    private String avatarProximoJogo = "avatar_proximo_jogo.png";
    private FirebaseAuth mAuth;
    private HelperJogador helperUsuario;
    public GoogleApiClient mGoogleApiClient;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button btLogin;
    private GoogleAuthFragment googleAuthFragment;
    private FragmentManager fragmentManager;
    private static final int RESOLVE_HINT = 101;
    private String telefoneUser;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        requestStoragePermission();
        btLogin = findViewById(R.id.sign_in_button);
        fragmentManager = this.getSupportFragmentManager();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            //toolbar.setVisibility(View.GONE);
            /*try {
                usuarioInformaTelefone();
            } catch (IntentSender.SendIntentException e) {
                Log.d(this.getClass().getName().toUpperCase(),"ERRO CAPTURAR FONE USER!");
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("foneUser", telefoneUser);*/
            googleAuthFragment = new GoogleAuthFragment();
            //googleAuthFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.container, googleAuthFragment).commit();
            inicializaTela();

        } else {
            inicializaTela();
            fragmentManager.beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();

        }


        leituraAvatar();
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void inicializaTela() {

        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setVisibility(View.VISIBLE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View hView = navigationView.getHeaderView(0);
        ivAvatar = hView.findViewById(R.id.ivAvatar);
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

    }

    /*Login*/

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (telefoneUser == null || telefoneUser == "") {
            try {
                usuarioInformaTelefone();
            } catch (IntentSender.SendIntentException e) {
                Log.d(this.getClass().getName().toUpperCase(), "ERRO CAPTURAR FONE USER!");
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
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

        } else if (id == R.id.drawer_criar_time) {
            fragmentManager.beginTransaction().replace(R.id.container, new ListaJogadoresTime()).commit();
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.container, new CriarBannerConfrontoFragment()).commit();
        } else if (id == R.id.drawer_cadastrar_arena) {

            fragmentManager.beginTransaction().replace(R.id.container, new ArenaFragment()).commit();
        } else if (id == R.id.jogos_passados) {
            fragmentManager.beginTransaction().replace(R.id.container, new ListaEventosPassadosAgenda()).commit();
        } else if (id == R.id.logout) {
            mAuth.signOut();

            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            updateUI();


                        }
                    });


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI() {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new GoogleAuthFragment()).commit();
        navigationView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("APP_DEBUG", String.valueOf(requestCode));

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if(credential != null){
                    credential.getId();// <-- E.164 format phone number on 10.2.+ devices
                    telefoneUser = credential.getId();
                }

            }
        }


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

    private void usuarioInformaTelefone() throws IntentSender.SendIntentException {

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();

        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();



        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                apiClient, hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RESOLVE_HINT, null, 0, 0, 0);
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
