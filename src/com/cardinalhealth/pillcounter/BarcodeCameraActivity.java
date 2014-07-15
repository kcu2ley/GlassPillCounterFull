package com.cardinalhealth.pillcounter;

import android.content.Intent;

public class BarcodeCameraActivity extends BaseCameraActivity {
	private boolean isOnline=false;
	
	@Override
	protected void handlePicture(byte[] data) {
		Intent intent = null;
		if (!isOnline) {
			intent = new Intent(this,OnlineInventoryActivity.class);
			getBarcodeData("016500080121", intent);
			
			isOnline = true;
		}
		else {
			intent = new Intent(this,CurrentInventoryActivity.class);
			
			isOnline = false;
		}
		startActivity(intent);
	}
    
    private void getBarcodeData(String barcode, Intent nextIntent) {
    	if (barcode.equals("016500080121")) {
	        nextIntent.putExtra("ndc", barcode);
	        nextIntent.putExtra("description", "Ibuprofen 10mg");
	        nextIntent.putExtra("online_count", 22);
	        nextIntent.putExtra("current_count", 22);
    	}
    	else {
	        nextIntent.putExtra("ndc", barcode);
	        nextIntent.putExtra("description", "Oxycodone 10mg");
	        nextIntent.putExtra("online_count", 17);
	        nextIntent.putExtra("current_count", 14);
    	}
    }

}
