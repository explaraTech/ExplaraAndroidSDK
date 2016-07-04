package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.explara.explara_ticketing_sdk.attendee.AttendeeManager;
import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.AmazonFileUploadUtil;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by anudeep on 08/01/16.
 */
public class AttendeeFormActivity extends BaseWithOutNavActivity {
    private static final String TAG = AttendeeFormActivity.class.getSimpleName();
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes TransferUtility, always do this before using it.
        transferUtility = AmazonFileUploadUtil.getTransferUtility(this);
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, AttendeeFormFragment.newInstance(getIntent()), TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Attendee Form");
    }


    @Override
    public void onBackPressed() {
        //     super.onBackPressed();
        if (getIntent().getExtras().getBoolean(ConstantKeys.BundleKeys.FROM_NOTIFICATION, false)) {
            launchHome();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                String path = getPath(uri);
                beginUpload(path, requestCode);
            } catch (URISyntaxException e) {
                Toast.makeText(this,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Log.e(TAG, "Unable to upload file from the given uri");
            }
        } else {
            AttendeeDetailsResponseDto.AttendeeDto attendeeDto = getAttendeeDtoByRequestcode(requestCode);
            if (attendeeDto != null) {
                getAttendeeBaseLayoutByRequestCode(attendeeDto).updateFileUploadStatus(requestCode, TransferState.PAUSED);
            }
        }
    }

    /*
     * Begins to upload the file specified by the file path.
     */
    private void beginUpload(String filePath, int requestCode) {
        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, "android_mobile" + file.getName() + ts,
                file);
        observer.setTransferListener(new UploadListener(requestCode, "android_mobile" + file.getName() + ts));

       /* observers.add(observer);
        HashMap<String, Object> map = new HashMap<String, Object>();
        AmazonFileUploadUtil.fillMap(map, observer, false);
        transferRecordMaps.add(map);

        simpleAdapter.notifyDataSetChanged(); */
    }

    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
     * A TransferListener class that can listen to a upload task and be notified
     * when the status changes.
     */
    private class UploadListener implements TransferListener {

        int requestCode;
        String filePath;

        UploadListener(int requestCode, String filePath) {
            this.requestCode = requestCode;
            this.filePath = filePath;
        }

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error during upload: " + id);
            //updateList();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            //updateList();
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d("attendeeSequenceState", newState + "");
            AttendeeDetailsResponseDto.AttendeeDto attendeeDto = getAttendeeDtoByRequestcode(requestCode);
            if (attendeeDto != null) {
                attendeeDto.fileName = Constants.CDN_URL + filePath;
                attendeeDto.fileUploadStatus = newState;
                if (TransferState.COMPLETED.equals(newState) || TransferState.FAILED.equals(newState) || TransferState.CANCELED.equals(newState)) {
                    getAttendeeBaseLayoutByRequestCode(attendeeDto).updateFileUploadStatus(requestCode, newState);
                }
            }
        }
    }

    // For file upload
    private AttendeeCustomLayout getAttendeeBaseLayoutByRequestCode(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        int attendeeSequence = AttendeeManager.getInstance().getAttendeeSequenceByAttendeeDto(attendeeDto);
        Log.d("attendeeSequence", attendeeSequence + "");
        AttendeeFormFragment attendeeFormFragment = (AttendeeFormFragment) FragmentHelper.getFragment(AttendeeFormActivity.this, R.id.fragment_container);
        //attendeeFormFragment.mEventsViewPager.setCurrentItem(attendeeSequence);
        AttendeeFieldItemsFragment attendeeFieldItemsFragment = (AttendeeFieldItemsFragment) attendeeFormFragment.mAttendeePagerAdapter.getFragment(attendeeSequence);
        return attendeeFieldItemsFragment.mAttendeeBaseLayout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mItem = menu.findItem(R.id.create_event);
        mItem.setVisible(false);
        return true;
    }

    //For file upload
    private AttendeeDetailsResponseDto.AttendeeDto getAttendeeDtoByRequestcode(int requestCode) {
        AttendeeDetailsResponseDto.AttendeeDto attendeeDto = AttendeeManager.getInstance().getAttendeeDtoByRequestCode(requestCode);
        if (attendeeDto != null) {
            if (ConstantKeys.AttendeeFormTypes.FILE.equals(attendeeDto.type)) {
                return attendeeDto;
            }
        }
        return null;
    }

}
