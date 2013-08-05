package com.gatekeeper.breakeven;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment{
	private Cursor myCursor;
	private TransactionDbAdapter dbHelper;
	private boolean resumed;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.activity_list, container, false);
		resumed = false;
		return v;
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		dbHelper = new TransactionDbAdapter(getActivity());
		dbHelper.open();
		
		Log.i("attach", "finished");
	}
	@Override
	public void onResume(){
		super.onResume();
		resumed = true;
		updateBalance();
		updateTransactionList();
	}
	
	private void updateTransactionList(){
		myCursor = dbHelper.fetchAllTransactions();
		final ListView list = (ListView)getView().findViewById(R.id.mainList);
		list.setAdapter(new RecentCursorAdapter(getActivity(),myCursor));
	}
	
	private void updateBalance(){
		final TextView textView = (TextView) getView().findViewById(R.id.balance);
		textView.setText(String.valueOf(dbHelper.getBalance()));
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	
		if (isVisibleToUser) {
			if(resumed){
				updateBalance();
				updateTransactionList();
			}
		}else{
			Log.i("list", "invisible");
		}

	}
}