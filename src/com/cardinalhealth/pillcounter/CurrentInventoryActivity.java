package com.cardinalhealth.pillcounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class CurrentInventoryActivity extends Activity {
	private static String TAG="CurrentInventory";
	
    /**
     * Handler used to post requests to start new activities so that the menu closing animation
     * works properly.
     */
    private final Handler mHandler = new Handler();

    /** Listener that displays the options menu when the touchpad is tapped. */
    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (gesture == Gesture.TAP) {
                mAudioManager.playSoundEffect(Sounds.TAP);
                openOptionsMenu();
                return true;
            } else {
                return false;
            }
        }
    };

    /** Audio manager used to play system sound effects. */
    private AudioManager mAudioManager;

    /** Gesture detector used to present the options menu. */
    private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_inventory);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);

		TextView textViewDesc = (TextView) findViewById(R.id.textViewDesc);
		textViewDesc.setText(getIntent().getStringExtra("description"));

		int onlineCount = getIntent().getIntExtra("online_count",0);
		int currentCount = getIntent().getIntExtra("current_count",0);
		TextView textViewNumber = (TextView) findViewById(R.id.textViewOnlineCount);
		textViewNumber.setText(Integer.toString(onlineCount));
		textViewNumber = (TextView) findViewById(R.id.textViewCurrentCount);
		textViewNumber.setText(Integer.toString(currentCount));
	
		Log.d(TAG,"OnlineCount=" + onlineCount + ", currentCount=" + currentCount);
		
		ViewGroup layout = (ViewGroup)findViewById(R.id.layoutData);
		if (onlineCount > currentCount)
			layout.setBackgroundResource(R.drawable.not_ok_mark);
		else
			layout.setBackgroundResource(R.drawable.ok_mark);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.counted_inventory, menu);
        return true;
    }

    /**
     * The act of starting an activity here is wrapped inside a posted {@code Runnable} to avoid
     * animation problems between the closing menu and the new activity. The post ensures that the
     * menu gets the chance to slide down off the screen before the activity is started.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The startXXX() methods start a new activity, and if we call them directly here then
        // the new activity will start without giving the menu a chance to slide back down first.
        // By posting the calls to a handler instead, they will be processed on an upcoming pass
        // through the message queue, after the animation has completed, which results in a
        // smoother transition between activities.
        switch (item.getItemId()) {
            case R.id.update_inventory:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateInventory();
                    }
                });
                return true;

            case R.id.cancel:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                });
                return true;

            default:
                return false;
        }
    }

    private void updateInventory() {
    	startNewScan();
    }
    
    private void cancel() {
    	startNewScan();
    }
    
    private void startNewScan() {
        startActivity(new Intent(this, BarcodeScannerActivity.class));
//        startActivity(new Intent(this, BarcodeCameraActivity.class));
        finish();
    	
    }
}
