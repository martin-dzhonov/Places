package com.garfield.places;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddPlaceActivity extends Activity{

	Context context = this;
	private static final int IMAGE_PICKER_SELECT = 999;
	private GoogleMap googleMap;
	private double currLatitute;
	private double currLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_place);

		// TODO: Sofia centre coordinates
		currLatitute =  42.6975100;
		currLongitude = 23.3241500;

		initNumberPicker();
		initMap();

		final Button imgButton = (Button) findViewById(R.id.Btn_add_place_img);
		imgButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, IMAGE_PICKER_SELECT);
			}
		});

		final Button saveButton = (Button) findViewById(R.id.Btn_save_place);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new EverlivePost().execute();
			}
		});
	}



	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_PICKER_SELECT
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();

			Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

			ImageView iView = (ImageView) findViewById(R.id.IV_add_image);
			iView.setImageBitmap(yourSelectedImage);

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

	private void initNumberPicker() {
		NumberPicker np = (NumberPicker) findViewById(R.id.NP_add_place);
		np.setMaxValue(1000);
		np.setMinValue(1);
		np.setValue(30);
	}

	private void initMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.MAP_add_place)).getMap();
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
		try {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			googleMap.getUiSettings().setCompassEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			// LocationManager lm = (LocationManager)
			// getSystemService(Context.LOCATION_SERVICE);
			// Location location =
			// lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(currLatitute, currLongitude)).title(
					"Your location here");
			marker.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			googleMap.addMarker(marker);

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(currLatitute, currLongitude)).zoom(15)
					.build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

	            @Override
	            public void onMapClick(LatLng point) {
	            	googleMap.clear();
	                MarkerOptions marker = new MarkerOptions().position(
	                        new LatLng(point.latitude, point.longitude)).title("New Marker");
	        	    currLatitute = (int) (point.latitude);
	        	    currLongitude = (int) (point.longitude);
	                googleMap.addMarker(marker);
	            }
	        });

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private class EverlivePost extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		private EditText namEditText;
		private EditText descEditText;
		private ImageView imageView;
		private NumberPicker nPicker;

		protected void onPreExecute() {
			namEditText = (EditText) findViewById(R.id.ET_add_place_name);
			descEditText = (EditText) findViewById(R.id.ET_add_place_description);
			imageView = (ImageView) findViewById(R.id.IV_add_image);
			nPicker = (NumberPicker) findViewById(R.id.NP_add_place);
			progressDialog = ProgressDialog.show(AddPlaceActivity.this, "",
					"Saving. Please wait...", true);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {

				JSONObject obj = new JSONObject();
				obj.put("name", namEditText.getText().toString());
				obj.put("description", descEditText.getText().toString());

				JSONObject location = new JSONObject();
				location.put("longitude", currLongitude);
				location.put("latitude", currLatitute);
				obj.put("location", location);

				BitmapDrawable drawable = (BitmapDrawable) imageView
						.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				String imageData = encodeTobase64(bitmap);
				obj.put("image", imageData);

				obj.put("capacity", String.valueOf(nPicker.getValue()));

				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost(
						"https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places");

				// passes the results to a string builder/entity
				StringEntity se = new StringEntity(obj.toString());

				// sets the post request as the resulting string
				httpPost.setEntity(se);

				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				// 8. Execute POST request to the given URL
				HttpResponse httpResponse = httpclient.execute(httpPost);

			} catch (Exception e) {
				Log.e("HomeActivity", "Error loading JSON", e);
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			Toast.makeText(context, "Saved.", Toast.LENGTH_SHORT).show();
		}

	}
}