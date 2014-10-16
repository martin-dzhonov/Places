package com.garfield.places.places;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import android.content.res.AssetFileDescriptor;
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
import android.widget.Toast;

import com.garfield.places.HomeActivity;
import com.garfield.places.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SuggestPlaceActivity extends Activity implements OnClickListener,
		LocationListener {

	Context context = this;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int IMAGE_PICKER_SELECT = 999;
	private GoogleMap googleMap;
	private double currLatitute;
	private double currLongitude;
	private Button addImgButton;
	private Button addPhotoButton;
	private LocationManager locationManager;
	private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest_place);
		// Sofia city centre coordinates
		currLatitute = 42.6975100;
		currLongitude = 23.3241500;
		tryFindCoordinates();
		initMap();
		addImgButton = (Button) findViewById(R.id.Btn_add_place_img);
		addImgButton.setOnClickListener(this);
		addPhotoButton = (Button) findViewById(R.id.Btn_add_place_photo);
		addPhotoButton.setOnClickListener(this);

		final Button saveButton = (Button) findViewById(R.id.Btn_suggest_place);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new EverlivePost().execute();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.Btn_add_place_img) {
			Intent addImageIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
			startActivityForResult(addImageIntent, IMAGE_PICKER_SELECT);
		}

		if (v.getId() == R.id.Btn_add_place_photo) {
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		currLatitute = (double) (location.getLatitude());
		currLongitude = (double) (location.getLongitude());
		Log.e("ONLOCATIONCHANGED", String.valueOf(currLatitute));
		Log.e("ONLOCATIONCHANGED", String.valueOf(currLongitude));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	private void tryFindCoordinates() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			Log.e("LOCATION NOT NULL", "Provider " + provider
					+ " has been selected.");
			Toast.makeText(context,
					"Provider " + provider + " has been selected.",
					Toast.LENGTH_SHORT).show();
			onLocationChanged(location);
		} else {
			Log.e("LOCATION NULL", "Provider " + provider
					+ " has been selected.");
			Toast.makeText(context, "Provider disabled, can't find location",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == IMAGE_PICKER_SELECT)
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
		if (requestCode == REQUEST_IMAGE_CAPTURE
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = data.getData();
			Bitmap yourSelectedImage = readBitmap(selectedImage);
			ImageView iView = (ImageView) findViewById(R.id.IV_add_image);
			iView.setImageBitmap(yourSelectedImage);

		}
	}

	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = this.getContentResolver().openAssetFileDescriptor(
					selectedImage, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bm = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	private static String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	public static void clearBitmap(Bitmap bm) {

		bm.recycle();

		System.gc();

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
			if (googleMap != null) {
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.getUiSettings().setZoomGesturesEnabled(true);
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(currLatitute, currLongitude)).title(
						"Your location here");
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				googleMap.addMarker(marker);
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(currLatitute, currLongitude))
						.zoom(14).build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
				googleMap
						.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

							@Override
							public void onMapClick(LatLng point) {
								googleMap.clear();
								MarkerOptions marker = new MarkerOptions()
										.position(
												new LatLng(point.latitude,
														point.longitude))
										.title("New Marker");
								currLatitute = (double) (point.latitude);
								currLongitude = (double) (point.longitude);
								googleMap.addMarker(marker);
							}
						});
			}
		} catch (Exception e) {
			Toast.makeText(context, "Unable to initialize Google Maps.",
					Toast.LENGTH_SHORT).show();
		}
	}

	private boolean validateInfo(String name, String desc, String website,
			String phoneNumber, String openTime) {
		if (name.equals("") || desc.equals("") || website.equals("")
				|| phoneNumber.equals("") || openTime.equals("")) {
			return false;
		}

		return true;
	}

	private class EverlivePost extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progressDialog;
		private EditText nameEditText;
		private EditText descEditText;
		private ImageView imageView;
		private EditText websiteEditText;
		private EditText phoneNumberEditText;
		private EditText openFromToEditText;

		protected void onPreExecute() {
			nameEditText = (EditText) findViewById(R.id.ET_add_place_name);
			descEditText = (EditText) findViewById(R.id.ET_add_place_description);
			imageView = (ImageView) findViewById(R.id.IV_add_image);
			websiteEditText = (EditText) findViewById(R.id.ET_add_place_website);
			phoneNumberEditText = (EditText) findViewById(R.id.ET_add_place_phone_number);
			openFromToEditText = (EditText) findViewById(R.id.ET_add_place_open_from_to);

			progressDialog = ProgressDialog.show(SuggestPlaceActivity.this, "",
					"Saving. Please wait...", true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				String name = nameEditText.getText().toString();
				String desc = descEditText.getText().toString();
				String website = websiteEditText.getText().toString();
				String phoneNumber = phoneNumberEditText.getText().toString();
				String openFromTo = openFromToEditText.getText().toString();

				boolean isValid = validateInfo(name, desc, website,
						phoneNumber, openFromTo);

				if (!isValid) {
					return false;
				}
				if (imageView.getDrawable() == null) {
					return false;
				}
				JSONObject obj = new JSONObject();
				obj.put("name", name);
				obj.put("description", desc);
				obj.put("website", website);
				obj.put("phoneNumber", phoneNumber);
				obj.put("openFromTo", openFromTo);
				obj.put("hasOnlineReservation", false);

				JSONObject location = new JSONObject();
				location.put("longitude", currLongitude);
				location.put("latitude", currLatitute);
				obj.put("location", location);

				BitmapDrawable drawable = (BitmapDrawable) imageView
						.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				String imageData = encodeTobase64(bitmap);
				obj.put("image", imageData);

				obj.put("capacity", String.valueOf(0));

				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost(
						"https://api.everlive.com/v1/BPHTkWwyt41jYxjq/Places");
				StringEntity se = new StringEntity(obj.toString());
				httpPost.setEntity(se);

				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				HttpResponse httpResponse = httpclient.execute(httpPost);

			} catch (Exception e) {
				Log.e("HomeActivity", "Error loading JSON", e);
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if (result == true) {
				Toast.makeText(context, "Saved.", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
			} else {
				boolean isValid = validateInfo(nameEditText.getText()
						.toString(), descEditText.getText().toString(),
						websiteEditText.getText().toString(),
						phoneNumberEditText.getText().toString(),
						openFromToEditText.getText().toString());

				if (!isValid) {
					Toast.makeText(context, "All text field are required.",
							Toast.LENGTH_SHORT).show();
				} else if (imageView.getDrawable() == null) {
					Toast.makeText(context, "Image is required.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Error saving to database.",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
