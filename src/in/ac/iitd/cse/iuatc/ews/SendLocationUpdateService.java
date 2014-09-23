package in.ac.iitd.cse.iuatc.ews;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class SendLocationUpdateService extends Service {

	private static final String TAG = "Location";

	private LocationManager locationManager;

	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(final Location location) {
			Log.d(TAG,"Location recieved");
			locationManager.removeUpdates(this);
			Thread thread = new Thread() {
				@Override
				public void run() {
					SendLocationUpdate(location);
				}
			};
			thread.start();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	};	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {	
		Log.d(TAG,"service started");
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
		/*final Location location = locationManager.getLastKnownLocation("gps");
		Thread thread = new Thread() {
			@Override
			public void run() {
				SendLocationUpdate(location);
			}
		};
		thread.start();*/
		return START_STICKY;		
	}


	private void SendLocationUpdate(Location location) {
		if(location==null) {
			stopSelf();	
			return;
		}
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String lastLat = sharedPreferences.getString("lastlat",""+location.getLatitude());
		String lastLng = sharedPreferences.getString("lastlng",""+location.getLongitude());
		Editor editor = sharedPreferences.edit();
		editor.putString("lastlat", ""+location.getLatitude());
		editor.putString("lastlng", ""+location.getLongitude());
		editor.commit();
		
		Uri.Builder b = Uri.parse(ServerParameters.TOPIC_MANAGER_URI).buildUpon();
		Uri uri =  b.appendQueryParameter("lastlat", lastLat)
	    .appendQueryParameter("lastlng", lastLng)
	    .appendQueryParameter("lat", ""+location.getLatitude())
	    .appendQueryParameter("lng", ""+location.getLongitude())
	    .appendQueryParameter("queue", Installation.id(this))
	    .build();
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.execute(new HttpGet(uri.toString()));
			Log.d(TAG,"Location sent");
		} catch (Exception e) {
			Log.e(TAG, "\n\n***** unable to reach server *****\n\n", e);
		}
		stopSelf();
	}

}
