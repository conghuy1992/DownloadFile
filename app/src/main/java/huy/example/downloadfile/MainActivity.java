package huy.example.downloadfile;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import huy.example.downloadfile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    String _URL = "http://yefschool.monamedia.net/Upload/Exercise/45eb9221-e41f-49d5-af70-a2f7ec14baaf.mp3";
    String TAG = "MainActivity";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        context=this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.btnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnDownload) {
            Log.d(TAG, "btnDownload");
            Utils.downloadFile(((Activity)context),_URL);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
