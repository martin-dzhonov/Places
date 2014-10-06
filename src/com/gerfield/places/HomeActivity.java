package com.gerfield.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomeActivity extends Activity {
	public final static String EXTRA_PLACE_NAME = "com.example.myfirstapp.EXTRA_PLACE_NAME";
	Context context = this;
	ListView list;
	String[] web = { "Place 1", "Place 2", "Place 3", "Place 4", "Place 5",
			"Place 6", "Place 7" };
	Integer[] imageId = { R.drawable.image2, R.drawable.image2,
			R.drawable.image2, R.drawable.image2, R.drawable.image2,
			R.drawable.image2, R.drawable.image2, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		CustomListViewAdapter adapter = new CustomListViewAdapter(
				HomeActivity.this, web, imageId);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, DetailsActivity.class);
				String message = web[position];
			    intent.putExtra(EXTRA_PLACE_NAME, message);
				startActivity(intent);
			}
		});
	}
}
