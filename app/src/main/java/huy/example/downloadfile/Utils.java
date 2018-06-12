package huy.example.downloadfile;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Utils {
    static String TAG = "Utils";

    public static void downloadFile(final Activity context, final String _URL) {
        Dexter.withActivity(context)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            startDownload(context, _URL);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public static String getFileName(String url) {
        String str[] = url.split("/");
        return str[str.length - 1];
    }

    public static void startDownload(Context context, String url) {
        showMsg(context, "Start Download");
        final long refer;
        String fileName = getFileName(url);
        Log.d(TAG, "fileName:" + fileName);
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(fileName).setTitle(getMsg(context, R.string.app_name));
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        refer = downloadManager.enqueue(request);

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver downloadcomplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long r = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (refer == r) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(r);
                    Cursor cursor = downloadManager.query(query);
                    cursor.moveToFirst();
                    //get status of the download
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
//                    int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//                    String saveFilePath = cursor.getString(filenameIndex);
//                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
//                    int reason = cursor.getInt(columnReason);
                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL:
                            if (context != null)
                                showMsg(context, "Download Completed");
                            break;
                        case DownloadManager.STATUS_FAILED:
                            showMsg(context, "Download Fail");
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            // do something                            break;
                        case DownloadManager.STATUS_PENDING:
                            // do something                            break;
                        case DownloadManager.STATUS_RUNNING:
                            // do something                            break;
                    }
                }
            }
        };
        context.registerReceiver(downloadcomplete, intentFilter);
    }

    public static String getMsg(Context context, int id) {
        if (context == null) return "";
        return context.getResources().getString(id);
    }

    public static void showMsg(Context context, String msg) {
        if (context != null) {
            try {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showMsg(Context context, int id) {
        if (context != null) {
            try {
                Toast.makeText(context, getMsg(context, id), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
