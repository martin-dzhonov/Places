package com.garfield.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

public class AddPlaceActivity extends Activity {
	Context context = this;
	private static final int IMAGE_PICKER_SELECT = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_place);

		NumberPicker np = (NumberPicker) findViewById(R.id.NP_add_place);
		np.setMaxValue(1000);
		np.setMinValue(1);
		np.setValue(30);
		final Button imgButton = (Button) findViewById(R.id.Btn_add_place_img);
		imgButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, IMAGE_PICKER_SELECT);
			}
		});
		final Button geoLocationButton = (Button) findViewById(R.id.Btn_add_place_geo);
		geoLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
			}
		});
		final Button saveButton = (Button) findViewById(R.id.Btn_save_place);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
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
			
			ImageView iView = (ImageView) findViewById(R.id.ImageView01);
			iView.setImageBitmap(yourSelectedImage);

		}
	}

}
