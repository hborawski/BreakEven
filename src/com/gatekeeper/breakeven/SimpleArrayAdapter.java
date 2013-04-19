package com.gatekeeper.breakeven;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SimpleArrayAdapter extends ArrayAdapter<Integer> {
	private Context context;
	private ArrayList<Integer> values;
	public SimpleArrayAdapter(Context context){
		super(context, R.layout.recent_row);
		this.context = context;
	}
	public SimpleArrayAdapter(Context context, ArrayList<Integer> values){
		super(context, R.layout.recent_row, values);
		this.context = context;
		this.values = values;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Log.i("adapter", String.valueOf(values.get(position)));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View recent_row_view = inflater.inflate(R.layout.recent_row,parent, false);
		TextView category = (TextView)recent_row_view.findViewById(R.id.category);
		TextView amount = (TextView)recent_row_view.findViewById(R.id.amount);
		if (values.get(position) >0){
			category.setText("add");
		}else{
			category.setText("remove");
		}
		amount.setText(String.valueOf(values.get(position)));
		
		
		return recent_row_view;
	}
}
