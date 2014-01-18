package com.headfirstlabs.nasadailyimage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NasaDailyImage extends Activity {
	
	private IotdHandler iotdHandler;
	ProgressDialog dialog;
	Handler handler;
	Bitmap image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		handler = new Handler();
		refreshFromFeed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nasa_daily_image, menu);
		return true;
	}
	
	private void resetDisplay(String title, String date,
			Bitmap image, StringBuffer description) {
		
		TextView titleView = (TextView) findViewById(R.id.imageTitle);
		titleView.setText(title);
		
		TextView dateView = (TextView) findViewById(R.id.imageDate);
		dateView.setText(date);
		
		ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
		imageView.setImageBitmap(image);
		
		TextView descriptionView = (TextView) findViewById(R.id.imageDescription);
		descriptionView.setText(description);
	}

	private void refreshFromFeed() {
		dialog = ProgressDialog.show(
				this,
				"Loading",
				"Loading the image of the Day");
		
		Thread th = new Thread() {
			public void run() {
				if (iotdHandler == null) {
					iotdHandler = new IotdHandler();
				}
				iotdHandler.processFeed();
//				image = getBitmap(iotdHandler.getUrl());
				image = iotdHandler.getImage();
				
				handler.post(
					new Runnable() {
						public void run() {
							resetDisplay(
								iotdHandler.getTitle(), 
								iotdHandler.getDate(),
								iotdHandler.getImage(),
								iotdHandler.getDescription());
							dialog.dismiss();
						}
					}
				);
			}
		};
		th.start();
	}
	
	public void onRefresh(View view) {
		refreshFromFeed();
	}
	
	public void onSetWallpaper(View view) {
		Thread th = new Thread() {
			public void run() {
				WallpaperManager wallpaperManager =
						WallpaperManager.getInstance(NasaDailyImage.this);
				try {
					wallpaperManager.setBitmap(image);
					handler.post(
							new Runnable() {
								public void run() {
									Toast.makeText(NasaDailyImage.this, "Wallpaper set", Toast.LENGTH_SHORT).show();
					}});
				}
				catch (Exception e) {
					e.printStackTrace();
					handler.post(
							new Runnable() {
								public void run() {
									Toast.makeText(NasaDailyImage.this, "Error setting wallpaper", Toast.LENGTH_SHORT).show();
					}});
				}
			}
		};
		th.start();
	}
}
