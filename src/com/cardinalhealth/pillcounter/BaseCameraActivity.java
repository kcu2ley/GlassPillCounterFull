package com.cardinalhealth.pillcounter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public abstract class BaseCameraActivity extends Activity {
	private static String TAG = "CameraActivity";
	private Camera mCamera;
	private CameraPreview mPreview;
    /** Audio manager used to play system sound effects. */
    private AudioManager mAudioManager;

    /** Gesture detector used to present the options menu. */
    private GestureDetector mGestureDetector;

    /** Listener that displays the options menu when the touchpad is tapped. */
    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (gesture == Gesture.TAP) {
                mAudioManager.playSoundEffect(Sounds.TAP);
            	handlePicture(null);
            	
            	// Close this activity.
//            	finish();
//                mCamera.takePicture(null, null, mPicture);
                return true;
            } else {
                return false;
            }
        }
    };

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	// Do something with the picture data.
        	
        	handlePicture(data);
        	
        	// Close this activity.
        	finish();
        }
    };
    
    protected abstract void handlePicture(byte[] data);
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"BaseCameraActivity onPause()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);
    }
	
    @Override
	protected void onResume() {
		Log.d(TAG,"BaseCameraActivity onResume()");

		super.onResume();
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
	}

	@Override
    protected void onPause() {
		Log.d(TAG,"BaseCameraActivity onPause()");

        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
        	mCamera.setPreviewCallback(null);
        	mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			Log.d(TAG,"Couldn't get camera!", e);
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}
}
