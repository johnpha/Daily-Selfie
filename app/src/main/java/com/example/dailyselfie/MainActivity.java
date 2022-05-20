package com.example.dailyselfie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final long INTERVAL_TWO_MINUTES = 2 * 60 * 1000L;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private SelfieRecordAdapter selfieRecordAdapter;
    private String currentSelfieName;
    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        ListView selfieList = (ListView) findViewById(R.id.selfie_list);
        selfieRecordAdapter = new SelfieRecordAdapter(getApplicationContext());

        selfieList.setAdapter(selfieRecordAdapter);
        selfieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelfieRecord selfieRecord = (SelfieRecord) selfieRecordAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, selfieRecord.getPath());
                startActivity(intent);
            }
        });
        createSelfieAlarm();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }
        if (id == R.id.action_delete_selected) {


            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);

            confirmDialog.setTitle("Delete record");
            confirmDialog.setMessage("Are you sure you want to delete the record is selected");



            confirmDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteSelectedSelfies();
                }


            });
            confirmDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = confirmDialog.create();
            alertDialog.show();

            return true;
        }
        if (id == R.id.action_delete_all) {
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);

            confirmDialog.setTitle("Delete record");
            confirmDialog.setMessage("Are you sure you want to delete all");



            confirmDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteAllSelfies();
                        }


                    });
            confirmDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog = confirmDialog.create();
            alertDialog.show();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private File createImageFile() throws IOException {
        currentSelfieName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imageFile = File.createTempFile( currentSelfieName,".jpg", getExternalFilesDir(null));
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void pictureIntent(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void dispatchTakePictureIntent() {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }

        else {
            pictureIntent();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_CAMERA_PERMISSION_CODE){
            Toast.makeText(this,"Camera permission granted", Toast.LENGTH_SHORT).show();
            pictureIntent();
        }
        else{
            Toast.makeText(this,"Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File photoFile = new File(currentPhotoPath);

            File selfieFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), currentSelfieName + ".jpg");
            photoFile.renameTo(selfieFile);

            SelfieRecord selfieRecord = new SelfieRecord(Uri.fromFile(selfieFile).getPath(), currentSelfieName);

            selfieRecordAdapter.add(selfieRecord);
        }
        else {
            File photoFile = new File(currentPhotoPath);
            photoFile.delete();
        }


    }

    private void deleteSelectedSelfies() {
        ArrayList<SelfieRecord> selectedSelfies = selfieRecordAdapter.getSelectedRecords();
        for (SelfieRecord selfieRecord : selectedSelfies) {
            File selfieFile = new File(selfieRecord.getPath());
            selfieFile.delete();
        }
        selfieRecordAdapter.clearSelected();
    }

    private void deleteAllSelfies() {
        for (SelfieRecord selfieRecord : selfieRecordAdapter.getAllRecords()) {
            File selfieFile = new File(selfieRecord.getPath());
            selfieFile.delete();
        }
        selfieRecordAdapter.clearAll();
    }

    private void createSelfieAlarm() {
        Intent intent = new Intent(this, SelfieNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INTERVAL_TWO_MINUTES,
                INTERVAL_TWO_MINUTES,
                pendingIntent);
    }


}