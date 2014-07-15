package com.cardinalhealth.pillcounter;

import android.content.Intent;

public class PillCameraActivity extends BaseCameraActivity {

	@Override
	protected void handlePicture(byte[] data) {
		Intent nextIntent = new Intent(this,CurrentInventoryActivity.class);
		nextIntent.putExtras(getIntent().getExtras());
		startActivity(nextIntent);

	}

}
