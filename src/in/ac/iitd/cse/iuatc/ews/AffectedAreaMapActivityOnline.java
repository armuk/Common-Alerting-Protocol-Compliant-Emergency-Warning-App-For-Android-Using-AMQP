package in.ac.iitd.cse.iuatc.ews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

public class AffectedAreaMapActivityOnline extends FragmentActivity {

	private GoogleMap mMap;
	LatLng center;
	double radius;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		double lat = (Double) getIntent().getSerializableExtra("LAT");
		double lng = (Double) getIntent().getSerializableExtra("LNG");
		this.center = new LatLng(lat, lng);

		this.radius = (Double) getIntent().getSerializableExtra("RADIUS");

		setContentView(R.layout.activity_affected_area_map);
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (this.mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			this.mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (this.mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		this.mMap.addCircle(new CircleOptions().center(this.center)
				.radius(this.radius).strokeColor(Color.argb(128, 0, 0, 255))
				.strokeWidth(3).fillColor(Color.argb(64, 0, 0, 255)));

		this.mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				LatLngBounds.Builder builder = LatLngBounds.builder();
				LatLngBounds bounds = builder
						.include(
								SphericalUtil
										.computeOffset(
												AffectedAreaMapActivityOnline.this.center,
												AffectedAreaMapActivityOnline.this.radius,
												0))
						.include(
								SphericalUtil
										.computeOffset(
												AffectedAreaMapActivityOnline.this.center,
												AffectedAreaMapActivityOnline.this.radius,
												90))
						.include(
								SphericalUtil
										.computeOffset(
												AffectedAreaMapActivityOnline.this.center,
												AffectedAreaMapActivityOnline.this.radius,
												180))
						.include(
								SphericalUtil
										.computeOffset(
												AffectedAreaMapActivityOnline.this.center,
												AffectedAreaMapActivityOnline.this.radius,
												270)).build();
				// Move camera.
				AffectedAreaMapActivityOnline.this.mMap
						.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,
								0));
				// Remove listener to prevent position reset on camera move.
				AffectedAreaMapActivityOnline.this.mMap
						.setOnCameraChangeListener(null);
			}
		});
	}

}