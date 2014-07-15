package com.cardinalhealth.pillcounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class OnlineInventoryActivity extends Activity {

	/** Listener that displays the options menu when the touchpad is tapped. */
	private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
		@Override
		public boolean onGesture(Gesture gesture) {
			if (gesture == Gesture.TAP) {
				mAudioManager.playSoundEffect(Sounds.TAP);
				takePillPicture();
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
		setContentView(R.layout.activity_online_inventory);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mGestureDetector = new GestureDetector(this)
				.setBaseListener(mBaseListener);
		
		TextView textViewDesc = (TextView) findViewById(R.id.textViewDesc);
		textViewDesc.setText(getIntent().getStringExtra("description"));
		
		TextView textViewNumber = (TextView) findViewById(R.id.textViewOnlineCount);
		textViewNumber.setText(Integer.toString(getIntent().getIntExtra("online_count",0)));
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		return mGestureDetector.onMotionEvent(event);
	}

	public void takePillPicture() {
		Intent nextIntent = new Intent(this,PillCameraActivity.class);
//		Intent nextIntent = new Intent(this,CurrentInventoryActivity.class);
		nextIntent.putExtras(getIntent().getExtras());
		startActivity(nextIntent);		
		finish();
	}
}
