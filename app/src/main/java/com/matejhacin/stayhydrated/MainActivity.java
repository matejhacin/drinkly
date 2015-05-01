package com.matejhacin.stayhydrated;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.matejhacin.receivers.AlarmReceiver;

import info.hoang8f.widget.FButton;


public class MainActivity extends Activity implements View.OnClickListener {

	// Public static variables
	public static int DEFAULT_POSTPONE_DURATION = 900000; // 15mins
	public static int DEFAULT_INTERVAL_DURATION = 7200000; // 120mins

	// Views
	FButton startButton, stopButton;

	// Other
	SharedPreferences sp;

	// Ad
	private InterstitialAd ad;
	private String AD_ID = "MY_ADMOB_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the ad
		ad = new InterstitialAd(getApplicationContext());
		ad.setAdUnitId(AD_ID);
		AdRequest adRequest = new AdRequest.Builder().build();
		ad.loadAd(adRequest);

        /* SHARED PREFERENCES
        isRunning - boolean weather notifications are running or not
        */
		sp = getSharedPreferences("settings", 0);

		// View variables
		startButton = (FButton) findViewById(R.id.startButton);
		stopButton = (FButton) findViewById(R.id.stopButton);

		// Click listeners
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);

		// Enable/Disable buttons
		if (sp.getBoolean("isRunning", false)) {
			disableButton(startButton);
		} else {
			disableButton(stopButton);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case (R.id.startButton):
				new AlarmReceiver().setAlarm(getApplicationContext(), DEFAULT_INTERVAL_DURATION);
				sp.edit().putBoolean("isRunning", true).apply();
				disableButton(startButton);
				enableButton(stopButton);
				showStartDialog();
				break;
			case (R.id.stopButton):
				new AlarmReceiver().cancelAlarm(getApplicationContext());
				sp.edit().putBoolean("isRunning", false).apply();
				disableButton(stopButton);
				enableButton(startButton);
				startButton.setEnabled(true);
				break;
		}
	}

	public void showStartDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Done!")
				.setMessage("When the notification appears (in 2 hours), click \"Later!\" to get another notification in 15 minutes.\n\nA one time advertisment will now be shown.")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						displayInterstitial();
						finish();
					}
				})
				.setCancelable(false)
				.show();
	}

	private void disableButton(FButton fBtn) {
		fBtn.setEnabled(false);
		fBtn.setButtonColor(getResources().getColor(R.color.gray_disabled));
		fBtn.setShadowColor(getResources().getColor(R.color.gray_disabled_shadow));
		fBtn.setTextColor(getResources().getColor(R.color.text_button_disabled));
	}

	private void enableButton(FButton fBtn) {
		fBtn.setEnabled(true);
		fBtn.setButtonColor(getResources().getColor(R.color.blue_enabled));
		fBtn.setShadowColor(getResources().getColor(R.color.blue_enabled_shadow));
		fBtn.setTextColor(getResources().getColor(R.color.text_button_enabled));
	}

	public void displayInterstitial() {
		if (ad.isLoaded()) {
			ad.show();
		}
	}
}
