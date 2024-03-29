package mx.edu.utng.mmacias.shareimage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class ShareActivity extends AppCompatActivity  {
    Bitmap bmp;
    private final String TEMPORAL_PICTURE_NAME = "temporal.jpg";
    //creamos los botones paracompartir
    private Button botonCompartir;
    //Constantes para subir foto
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "media";

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private final int  MY_PERMISSIONS_REQUEST_CAMERA = 200;

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView mSetImage;
    private Button mOptionButton;

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mOptionButton = (Button) findViewById(R.id.btn_photo);
        mSetImage = (ImageView) findViewById(R.id.img_photo);

        //asignamos a los botones e la actividad los botones del diseño.
        //el error se corregir al parsear
        botonCompartir = (Button) findViewById(R.id.buttonShare);


        //ahora le asignamos la accion a relizar a los botones
        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

        //ahora le asignamos la accion a relizar a los botones
        botonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aqui asginamos la accion a realizar
                //importamos las librerias con alt+intro

                mSetImage.buildDrawingCache();
                Bitmap bitmap = mSetImage.getDrawingCache();

                /***** COMPARTIR IMAGEN *****/
                try {
                    File file = new File(getCacheDir(), bitmap + ".png");
                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/png");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "El mejor blog de android http://javaheros.blogspot.pe/");
                startActivity(Intent.createChooser(intent, "Share with"));*/
            }
        });

       int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }
    }


    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }



    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();

        String path =Environment.getExternalStorageDirectory()+file.separator +
                MEDIA_DIRECTORY+file.separator + TEMPORAL_PICTURE_NAME;
        File newfile = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(newfile));
        startActivityForResult(intent,PHOTO_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode == RESULT_OK){
                    String dir =Environment.getExternalStorageDirectory() + File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
                break;
            case SELECT_PICTURE:
                Uri path = data.getData();
                mSetImage.setImageURI(path);
                break;

        }
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap= BitmapFactory.decodeFile(dir);
        mSetImage.setImageBitmap(bitmap);
    }
}

