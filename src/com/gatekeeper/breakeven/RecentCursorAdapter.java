package com.gatekeeper.breakeven;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentCursorAdapter extends CursorAdapter {
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	public RecentCursorAdapter(Context context, Cursor c){
		super(context,c);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View newView(Context context, Cursor c, ViewGroup parent){
		View v = mLayoutInflater.inflate(R.layout.recent_row,parent, false);
		return v;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c){
		int amount = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_AMOUNT)));
		String category = c.getString(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_CATEGORY));
		
		TextView amount_text = (TextView)view.findViewById(R.id.amount);
		if (amount_text != null){
			if (amount>0){
				amount_text.setText(String.valueOf(amount));
			}else{
				amount_text.setText(String.valueOf(amount*-1));
			}
		}
		
		TextView cat_text = (TextView)view.findViewById(R.id.category);
		if (cat_text!=null){
			cat_text.setText(category);
		}
		
		ImageView img = (ImageView)view.findViewById(R.id.pic);
		if(amount>0){
			img.setImageResource(R.drawable.plus);
		}else{
			img.setImageResource(R.drawable.minus);
		}
			
		
	}
}
