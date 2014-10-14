package com.garfield.places;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.garfield.places.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends Activity  {

	Context context = this;
	public final static String PLACE_ID = "com.example.myfirstapp.PLACE_ID";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		Intent intent = getIntent();
		final String placeId = intent.getStringExtra(HomeActivity.EXTRA_PLACE_ID);
		new LoadPlaceDetails().execute(placeId);

		final Button button = (Button) findViewById(R.id.Btn_make_reservation);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {			
				Intent reserveIntent = new Intent(context, ReservationActivity.class);
				reserveIntent.putExtra(PLACE_ID, placeId);
				startActivity(reserveIntent);
			}
		});
	}

	private class LoadPlaceDetails extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressDialog;
		private TextView nameTextView;
		private TextView descriptionTextView;
		private TextView capacityTextView;
		private ImageView imageView;
		private GoogleMap googleMap;

		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(DetailsActivity.this, "",
					"Loading. Please wait...", true);
			nameTextView = (TextView) findViewById(R.id.TV_details_name);
			descriptionTextView = (TextView) findViewById(R.id.TV_details_description);
			capacityTextView = (TextView) findViewById(R.id.TV_details_capacity);
			imageView = (ImageView) findViewById(R.id.IV_details_image);

			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.MAP_details)).getMap();
				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

		@Override
		protected JSONObject doInBackground(String... placeId) {
			JSONObject placeJsonObject = null;
			try {

				HttpClient hc = new DefaultHttpClient();
				Log.e("ASDASDASDA", placeId[0]);
				String path = "https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places/" +placeId[0];
				HttpGet get = new HttpGet(path);

				HttpResponse rp = hc.execute(get);

				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(rp.getEntity());
					JSONObject root = new JSONObject(result);
					placeJsonObject = root.getJSONObject("Result");
				}
			} catch (Exception e) {
				Toast.makeText(context, "Error connecting  to database",
						Toast.LENGTH_LONG).show();
			}
			return placeJsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			progressDialog.dismiss();
			try {
				String name = result.getString("name");
				String description = result.getString("description");
				String capacity = result.getString("capacity");
				String imageData = result.getString("image");
				JSONObject location = result.getJSONObject("location");
				double longitude = location.getDouble("longitude");
				double latitude = location.getDouble("latitude");
				nameTextView.setText(name);
				descriptionTextView.setText(description);
				capacityTextView.setText(capacity);
				byte[] imageAsBytes = Base64.decode(imageData.getBytes(),
						Base64.DEFAULT);
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(
						imageAsBytes, 0, imageAsBytes.length));
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude))
						.title("Your location here");
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				googleMap.addMarker(marker);
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(latitude, longitude)).zoom(15)
						.build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
