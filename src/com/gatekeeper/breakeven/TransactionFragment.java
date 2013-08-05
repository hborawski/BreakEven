package com.gatekeeper.breakeven;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TransactionFragment extends Fragment{
	private TransactionDbAdapter dbHelper;
	private boolean resumed;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		resumed = false;
		View v =  inflater.inflate(R.layout.activity_paid, container, false);
		Button button = (Button)v.findViewById(R.id.add);
		button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.i("listener", "button");
                addTransaction();
                clearFields();
            }
        });
		
		return v;
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		dbHelper = new TransactionDbAdapter(getActivity());
		dbHelper.open();
		
		Log.i("attach", "finished");
	}
	
	
	private void addTransaction(){
		View view = getView();
		EditText field = (EditText) view.findViewById(R.id.paycheck);
		int value = Integer.parseInt(field.getText().toString());
		EditText catText= (EditText) view.findViewById(R.id.categoryField);
		String category = catText.getText().toString();
		dbHelper.createTransaction(value, category);
		
	}
	
	private void clearFields(){
		View view = getView();
		EditText field = (EditText) view.findViewById(R.id.paycheck);
		field.setText("");
		EditText catText= (EditText) view.findViewById(R.id.categoryField);
		catText.setText("");
	}
}
