package com.garfield.places.places;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.garfield.places.HomeActivity;
import com.garfield.places.R;
import com.garfield.places.reservations.ReservationActivity;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetailsActivity extends Activity implements View.OnClickListener {
	private TextView nameTextView;
	private TextView descriptionTextView;
	private ImageView imageView;

	Context context = this;
	private String placeId;
	
	public final static String PLACE_ID = "com.example.myfirstapp.PLACE_ID";
	public final static String PLACE_NAME_KEY = "PLACE_NAME";
	public final static String DESCRIPTION_KEY = "DESCRIPTION";
	public final static String IMAGE_KEY = "IMAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_details);

		Intent intent = getIntent();
		placeId = intent.getStringExtra(HomeActivity.EXTRA_PLACE_ID);
		new LoadPlaceDetails().execute(placeId);

		final Button noteReservationButton = (Button)this.findViewById(R.id.Btn_note_reservation);
		final Button makeReservationOnlineButton = (Button)this.findViewById(R.id.Btn_make_reservation_online);
		
		noteReservationButton.setOnClickListener(this);
		makeReservationOnlineButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.Btn_note_reservation) {
			Intent reserveIntent = new Intent(context, ReservationActivity.class);
			Bundle info = new Bundle();
			
			info.putString(PLACE_ID, placeId);
			info.putString(PLACE_NAME_KEY, nameTextView.getText().toString());
			info.putString(DESCRIPTION_KEY, descriptionTextView.getText().toString());
			info.putString(IMAGE_KEY, encodeTobase64(((BitmapDrawable)imageView.getDrawable()).getBitmap()));
			
			reserveIntent.putExtras(info);
			startActivity(reserveIntent);
		}
		
		if (v.getId() == R.id.Btn_make_reservation_online) {
			// TODO: Open restourant website, to make reservation
		}
	}
	
	private static String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}
	
	private class LoadPlaceDetails extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressDialog;
		private GoogleMap googleMap;

		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(PlaceDetailsActivity.this, "",
					"Loading. Please wait...", true);
			nameTextView = (TextView) findViewById(R.id.TV_details_name);
			descriptionTextView = (TextView) findViewById(R.id.TV_details_description);
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
				// TODO: Visualize new data(website, phone number, open time)
				// TODO: Hide make reservation online if restaurant does not support such option
				String name = result.getString("name");
				String description = result.getString("description");
				String imageData = result.getString("image");
				JSONObject location = result.getJSONObject("location");
				double longitude = location.getDouble("longitude");
				double latitude = location.getDouble("latitude");
				nameTextView.setText(name);
				descriptionTextView.setText(description);
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
						.target(new LatLng(latitude, longitude)).zoom(14)
						.build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
