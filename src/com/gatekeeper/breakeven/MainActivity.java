package com.gatekeeper.breakeven;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends android.support.v4.app.FragmentActivity {
	public static final int PAID = 1;
	public static final int PAY = 2;
	public static final int UPDATE = 3;
	private static int myBalance=0;
	private Cursor myCursor;
	private TransactionDbAdapter dbHelper;
	
	private static final int DELETE_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//context = getApplicationContext();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		/* Needs to be moved to listfragment
		ListView list = (ListView)findViewById(R.id.mainList);
		registerForContextMenu(list);
		*/
		dbHelper = new TransactionDbAdapter(this);
		dbHelper.open();
		
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter{
		
		public SectionsPagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position){
			case 0:
				return new ListFragment();
			case 1:
				return new TransactionFragment();
			default:
				break;			
			}
			return new Fragment();
		}

		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position){
			switch(position){
			case 0:
				return "Transactions";
			case 1:
				return "New Transaction";
			default:
				return "Broken";
			}
		}
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		Log.i("state","onStart");
		
	}
	@Override
	protected void onStop(){
		super.onStop();
		Log.i("state","onStop");
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.i("state","onPause");
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		Log.i("state","onRestart");
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	/*
	public void fillProfiles(){
		final Spinner spin = (Spinner)findViewById(R.id.profiles);
		List<String> list = new ArrayList<String>();
		list.add("Transactions");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(dataAdapter);
		
	}
	*/

	public void toList(){
		mViewPager.setCurrentItem(0);
	}

	public void editTransaction(long id){
		Intent intent = new Intent(this, PaidActivity.class);
		intent.putExtra("CALL", UPDATE);
		intent.putExtra("id", id);
		Cursor c = dbHelper.fetchSingleTransaction(id);
		c.moveToFirst();
		int amount = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_AMOUNT)));
		String category = c.getString(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_CATEGORY));
		intent.putExtra("amount", amount);
		intent.putExtra("description", category);
		startActivityForResult(intent, UPDATE);
	}

}
