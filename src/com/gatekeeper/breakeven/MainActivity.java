package com.gatekeeper.breakeven;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends android.support.v4.app.FragmentActivity {
	public static final int PAID = 1;
	public static final int PAY = 2;
	public static final int UPDATE = 3;
	private TransactionDbAdapter dbHelper;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    
    //Terrible workaround
    private long editId;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		dbHelper = new TransactionDbAdapter(this);
		dbHelper.open();
		
		this.editId = Long.valueOf("-1");
		
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case R.id.menu_clear:
			this.clearList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		
		}
	}
	*/
	
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
	public void editTransaction(long id){
		Cursor c = dbHelper.fetchSingleTransaction(id);
		c.moveToFirst();
		int amount = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_AMOUNT)));
		String category = c.getString(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_CATEGORY));
		FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) mViewPager.getAdapter();
		TransactionFragment frag = (TransactionFragment) fragmentPagerAdapter.getItem(1);
		View v = findViewById(R.id.trans_frag);
		mViewPager.setCurrentItem(1);
		this.editId = id;
		frag.editTransaction(id, amount, category, v);
	}
	
	public void clearList(){
		dbHelper.clear();
		FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) mViewPager.getAdapter();
		ListFragment frag = (ListFragment) fragmentPagerAdapter.getItem(0);
		View v = findViewById(R.id.list_frag);
		//frag.updateBalance(v);
		frag.updateTransactionList(dbHelper.fetchAllTransactions(), v);
	}
	
	public long getId(){
		return this.editId;
	}
	public void clearId(){
		this.editId = Long.valueOf("-1");
	}
	public void toList(){
		mViewPager.setCurrentItem(0);
	}

}
