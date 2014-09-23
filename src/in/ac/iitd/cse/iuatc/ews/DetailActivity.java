package in.ac.iitd.cse.iuatc.ews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


public class DetailActivity extends Activity {

	AlertDetailsAdapter adapter;
	SimpleAlert alert;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);

		this.alert = (SimpleAlert) getIntent().getSerializableExtra("ALERT");
		ArrayList<StringPair> alertDetails = new ArrayList<StringPair>(
				this.alert.infoMap.values());

		this.adapter = new AlertDetailsAdapter(this, R.layout.detail_list_row,
				alertDetails);
		this.listView = (ListView) findViewById(R.id.listView1);
		this.listView.setAdapter(this.adapter);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.warning_details_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent showMap;
		switch (item.getItemId()) {
		case R.id.show_in_map_online: {
			if (this.alert.isMapDataRecieved()) {
				showMap = new Intent(
						this,
						in.ac.iitd.cse.iuatc.ews.AffectedAreaMapActivityOnline.class);
				showMap.putExtra("LAT", this.alert.getLat());
				showMap.putExtra("LNG", this.alert.getLng());
				showMap.putExtra("RADIUS", this.alert.getRadiusInMeters());
				startActivity(showMap);
			} else {
				Toast.makeText(this, "No map data received", Toast.LENGTH_LONG)
				.show();
			}
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
