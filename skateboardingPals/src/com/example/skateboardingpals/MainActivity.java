package com.example.skateboardingpals;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final int PICK_CONTACT_REQUEST = 0;
	private Uri contactUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		renderContact(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void renderContact(Uri uri) {
		TextView contactNameView =
				(TextView) findViewById(R.id.contact_name);
		TextView contactPhoneView =
				(TextView) findViewById(R.id.contact_phone);
		ImageView photoView =
				(ImageView) findViewById(R.id.contact_photo);
		
		if (uri == null) {
			contactNameView.setText("Select a contact");
			contactPhoneView.setText("");
			photoView.setImageBitmap(null);
		} else {
			contactNameView.setText(getDisplayName(uri));
			contactPhoneView.setText(getMobileNumber(uri));
			photoView.setImageBitmap(getPhoto(uri));
		}
		
	}
	
	public void onUpdateContact(View view) {
		startActivityForResult(
				new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
				PICK_CONTACT_REQUEST);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == PICK_CONTACT_REQUEST) {
			if (resultCode == RESULT_OK) {
				Log.d("Selection", intent.toString());
				contactUri = intent.getData();
				renderContact(intent.getData());
			}
		}
	}
	
	private String getDisplayName(Uri uri) {
		String displayName = null;
		
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor.moveToFirst()) {
			displayName = cursor.getString(
					cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		}
		cursor.close();
		return displayName;
	}
	
	private String getMobileNumber(Uri uri) {
		String phoneNumber = null;
		Cursor contactCursor = getContentResolver().query(
				uri, new String[]{ContactsContract.Contacts._ID},
				null, null, null);
		String id = null;
		if (contactCursor.moveToFirst()) {
			id = contactCursor.getString(
					contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
		}
		contactCursor.close();
		
		Cursor phoneCursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
				+ ContactsContract.CommonDataKinds.Phone.TYPE + " = "
				+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
				new String[] { id },
				null
				);
		if (phoneCursor.moveToFirst()) {
			phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
					ContactsContract.CommonDataKinds.Phone.NUMBER)
					);
		}
		phoneCursor.close();
		
		return phoneNumber;
	}
	
	private Bitmap getPhoto(Uri uri) {
		Bitmap photo = null;
		String id = null;
		Cursor contactCursor = getContentResolver().query(
				uri, new String[] {ContactsContract.Contacts._ID}, null, null, null);
		if (contactCursor.moveToFirst()) {
			id = contactCursor.getString(
					contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
		}
		contactCursor.close();
		try {
			InputStream input =
					ContactsContract.Contacts.openContactPhotoInputStream(
							getContentResolver(),
							ContentUris.withAppendedId(
									ContactsContract.Contacts.CONTENT_URI,
									new Long(id).longValue())
									);
			if (input != null) {
				photo = BitmapFactory.decodeStream(input);
			}
			input.close();
		} catch (IOException iox) { /* exception handing here */ }
		return photo;
	}
	
	public void onImCoolButtonClick(View view) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(
				getMobileNumber(contactUri),
				null, 
				"Babe, I'm Cool!", 
				null,
				null);
	}
}
