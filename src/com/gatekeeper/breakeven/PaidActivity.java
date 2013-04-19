package com.gatekeeper.breakeven;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaidActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paid);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		if (intent.getIntExtra("CALL",0) == MainActivity.PAY){
			Button b = (Button) findViewById(R.id.add);
			TextView title = (TextView)findViewById(R.id.title);
			title.setText("Pay up");
			b.setText("Remove");
			
		}
		EditText text = (EditText)findViewById(R.id.paycheck);
		text.requestFocus();
		InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void add(View v){
		EditText field = (EditText) findViewById(R.id.paycheck);
		int value = Integer.parseInt(field.getText().toString());
		EditText catText= (EditText) findViewById(R.id.categoryField);
		String category = catText.getText().toString();
		Intent data = new Intent();
		data.putExtra("VALUE",value);
		data.putExtra("CAT", category);
		if (getParent() == null) {
		    setResult(Activity.RESULT_OK, data);
		} else {
		    getParent().setResult(Activity.RESULT_OK, data);
		}
		InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboard.hideSoftInputFromWindow(((View)findViewById(R.id.paycheck)).getWindowToken(), 0);

		finish();
	}

}
