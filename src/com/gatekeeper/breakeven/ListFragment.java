package com.gatekeeper.breakeven;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment{
	private Cursor myCursor;
	private TransactionDbAdapter dbHelper;
	private boolean resumed;
	private static final int DELETE_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
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
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		ListView list = (ListView)getView().findViewById(R.id.mainList);
		registerForContextMenu(list);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo ){
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0,EDIT_ID,0,R.string.menu_edit);
		menu.add(0, DELETE_ID,0,R.string.menu_delete);
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                dbHelper.deleteTransaction(info.id);
                updateBalance();
                updateTransactionList(dbHelper.fetchAllTransactions());
                return true;
            case EDIT_ID:
            	((MainActivity)getActivity()).editTransaction(((AdapterContextMenuInfo)item.getMenuInfo()).id);
            	return true;
        }
        return super.onContextItemSelected(item);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		resumed = true;
		updateBalance();
		updateTransactionList(dbHelper.fetchAllTransactions());
	}
	
	public void updateTransactionList(Cursor c){
		this.updateTransactionList(c, null);
	}
	public void updateTransactionList(Cursor c, View v){
		if( v == null){ v= getView(); }
		final ListView list = (ListView)v.findViewById(R.id.mainList);
		list.setAdapter(new RecentCursorAdapter(getActivity(), c));
	}
	
	public void updateBalance(){
		this.updateBalance(null);
	}
	
	public void updateBalance(View v){
		if( v == null){ v= getView(); }
		final TextView textView = (TextView) v.findViewById(R.id.balance);
		if(dbHelper == null){
			dbHelper = new TransactionDbAdapter(getActivity());
			dbHelper.open();
		}
		String value = String.valueOf(dbHelper.getBalance());
		textView.setText(value);
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	
		if (isVisibleToUser) {
			if(resumed){
				updateBalance();
				updateTransactionList(dbHelper.fetchAllTransactions());
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().findViewById(R.id.mainList).getWindowToken(), 0);
			}
		}else{
			Log.i("list", "invisible");
		}

	}
}