package com.approsoft.opentokdemo;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46029492";
    private static String SESSION_ID = "2_MX40NjAyOTQ5Mn5-MTUxNDQ2MDU2MDYyN343U3ZxN2xJVDZsZFNIa3R4R3dzcjh2NnR-UH4";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjAyOTQ5MiZzaWc9MjJlNDFkZTJmNWRmYmJlYzc1ZTZkYTUwZmI4Y2FlYWU5OGI2YTMxNjpzZXNzaW9uX2lkPTJfTVg0ME5qQXlPVFE1TW41LU1UVXhORFEyTURVMk1EWXlOMzQzVTNaeE4yeEpWRFpzWkZOSWEzUjRSM2R6Y2poMk5uUi1VSDQmY3JlYXRlX3RpbWU9MTUxNDQ2MDYwOCZub25jZT0wLjgxMDM3MzMyOTg3NTMzMDgmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTUxNzA1MjYwOCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session session;
    private Publisher publisher;
    private Subscriber subscriber;

    private FrameLayout framePublisher, frameSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        framePublisher = (FrameLayout) findViewById(R.id.framePublisher);
        frameSubscriber = (FrameLayout) findViewById(R.id.frameSubscriber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);
    }

    @AfterPermissionGranted(RC_SETTINGS_SCREEN_PERM)
    private void requestPermissions(){
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(MainActivity.this, perms)){
            session = new Session.Builder(MainActivity.this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);

        }else{
            EasyPermissions.requestPermissions(MainActivity.this, "This application requires following permissions.", RC_SETTINGS_SCREEN_PERM, perms);
        }
    }

    /**
     * SessionListener Methods
     **/

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(MainActivity.this).build();
        publisher.setPublisherListener(MainActivity.this);

        framePublisher.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if(subscriber == null){
            subscriber = new Subscriber.Builder(MainActivity.this, stream).build();
            session.subscribe(subscriber);
            frameSubscriber.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if(subscriber!=null){
            subscriber = null;
            frameSubscriber.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }


    /**
     * PublisherListener Methods
     **/
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
