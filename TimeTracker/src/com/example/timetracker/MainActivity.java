package com.example.timetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	public static final int TIME_ENTRY_REQUEST_CODE = 1;
	
	private TimeTrackerAdapter timeTrackerAdapter;
	private TimeTrackerDatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TimeListDatabaseHelper openHelper = new TimeListDatabaseHelper(this);
		databaseHelper = new TimeTrackerDatabaseHelper(this);
		
		ListView listView = (ListView) findViewById(R.id.times_list);
		timeTrackerAdapter = new TimeTrackerAdapter(this, databaseHelper.getTimeRecordList());
		listView.setAdapter(timeTrackerAdapter);
	}

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.time_list_menu, menu);
		return true;
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.add_time_menu_item) {
			Intent intent = new Intent(this, AddTimeActivity.class);
			//startActivity(intent);
			startActivityForResult(intent, TIME_ENTRY_REQUEST_CODE);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TIME_ENTRY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String notes = data.getStringExtra("notes");
				String time = data.getStringExtra("time");
				
				databaseHelper.saveTimeRecord(time, notes);
				timeTrackerAdapter.changeCursor(databaseHelper.getTimeRecordList());
				
//				timeTrackerAdapter.addTimeRecord(new TimeRecord(time, notes));
//				timeTrackerAdapter.notifyDataSetChanged();
			}
		}
	}

}
