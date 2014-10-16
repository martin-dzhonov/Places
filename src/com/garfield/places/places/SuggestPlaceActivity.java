package com.garfield.places.places;

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

import com.garfield.places.HomeActivity;
import com.garfield.places.R;
import com.garfield.places.R.id;
import com.garfield.places.R.layout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SuggestPlaceActivity extends Activity implements OnClickListener {

	Context context = this;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int IMAGE_PICKER_SELECT = 999;
	private GoogleMap googleMap;
	private double currLatitute;
	private double currLongitude;
	private Button addImgButton;
	private Button addPhotoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest_place);

		// Sofia city centre coordinates
		currLatitute = 42.6975100;
		currLongitude = 23.3241500;

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
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(addImageIntent, IMAGE_PICKER_SELECT);
		}
		
		if (v.getId() == R.id.Btn_add_place_photo) {
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == IMAGE_PICKER_SELECT || requestCode == REQUEST_IMAGE_CAPTURE)
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
					.target(new LatLng(currLatitute, currLongitude)).zoom(14)
					.build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

				@Override
				public void onMapClick(LatLng point) {
					googleMap.clear();
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(point.latitude, point.longitude)).title(
							"New Marker");
					currLatitute = (double) (point.latitude);
					currLongitude = (double) (point.longitude);
					googleMap.addMarker(marker);
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private class EverlivePost extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progressDialog;
		private EditText namEditText;
		private EditText descEditText;
		private ImageView imageView;
		private NumberPicker nPicker;

		protected void onPreExecute() {
			namEditText = (EditText) findViewById(R.id.ET_add_place_name);
			descEditText = (EditText) findViewById(R.id.ET_add_place_description);
			imageView = (ImageView) findViewById(R.id.IV_add_image);
			progressDialog = ProgressDialog.show(SuggestPlaceActivity.this, "",
					"Saving. Please wait...", true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				String name = namEditText.getText().toString();
				String desc = descEditText.getText().toString();
				if (name.equals("") || desc.equals("")) {
					return false;
				}
				if (imageView.getDrawable() == null) {
					return false;
				}
				JSONObject obj = new JSONObject();
				obj.put("name", name);
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

				obj.put("capacity", String.valueOf(0));

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
				// TODO: make all input requred

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
				if (namEditText.getText().toString().equals("")
						|| descEditText.getText().toString().equals("")) {
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