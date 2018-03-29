package com.lavishweb.www.relate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.WebSettings;
import android.app.Activity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import android.util.Base64;
import java.io.OutputStream;
import android.graphics.BitmapFactory;
import com.squareup.picasso.Target;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.graphics.drawable.Drawable;


public class WebView extends AppCompatActivity{

    private static final int STORAGE_PERMISSION_CODE = 3094;
    private static final int PICK_IMAGE_REQUEST = 32;
    private String filePath;
    private Bitmap bitMap;
    private File imageFile;
    private File newImageFile;
    private Target mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);
        final android.webkit.WebView view = (android.webkit.WebView) findViewById(R.id.webView);
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setSupportMultipleWindows(false);
        view.setWebChromeClient(new WebChromeClient());

        class JavaScriptInterface {

            @JavascriptInterface
            public void getImageBase64(){
//                requestStoragePermission();
                showFileChooser();
            }

            @JavascriptInterface
            public String returnedImage(String path){
                view.loadUrl("javascript:alert('"+path+"')");
                return path;
            }


        }

        view.addJavascriptInterface(new JavaScriptInterface(), "Android");

        view.loadUrl("https://relate.lavishweb.com/settings");

    }

    private void requestStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null) {

            String dataString = data.getDataString();
            filePath = new File(dataString).getAbsolutePath();

            android.webkit.WebView view = (android.webkit.WebView) findViewById(R.id.webView);
            view.loadUrl("javascript:(function(){ alert('"+filePath+"');window.uploader.profileImage = '" + filePath + "'})()");


        }
    }

}
