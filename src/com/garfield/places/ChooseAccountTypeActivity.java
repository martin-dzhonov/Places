package com.garfield.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.orm.SugarApp;

public class ChooseAccountTypeActivity extends Activity {
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_account_type);
		
		final Button placeButton = (Button) findViewById(R.id.Btn_account_type_place);
		placeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AccountGeneral.USER_ACCOUNT_TYPE = AccountGeneral.ACCOUNT_TYPE_PLACE;
				SugarApp.getSugarContext().deleteDatabase("places.db");
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
			}
		});
		final Button customerButton = (Button) findViewById(R.id.Btn_account_type_customer);
		customerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AccountGeneral.USER_ACCOUNT_TYPE = AccountGeneral.ACCOUNT_TYPE_CUSTOMER;
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
			}
		});
	}
}
