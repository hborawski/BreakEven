package com.gatekeeper.breakeven;

import java.sql.Timestamp;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TransactionDbAdapter{
	private static final String DATABASE_NAME = "TransactionDB";
	private static String TABLE_NAME = "transactions";
	private static int DATABASE_VERSION = 1;
	public static final String KEY_DATE = "date";
	public static final String KEY_ROW_ID = "rowId";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_CATEGORY = "category";
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqlDb;
	
	
	private final Context mContext;
	
	private static final String TAG = "TransactionDb";
	
	private static final String DATABASE_CREATE =
	        "create table "+TABLE_NAME+" (_id integer primary key autoincrement, "
	        + KEY_DATE+" timestamp, "+KEY_AMOUNT+" text not null,"+ KEY_CATEGORY+" text not null);";
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }
	
	public TransactionDbAdapter(Context context){
		this.mContext = context;
	}
	
	public TransactionDbAdapter open() throws SQLException{
		dbHelper = new DatabaseHelper(mContext);
		sqlDb = dbHelper.getWritableDatabase();
		return this;
		
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void clear(){
		sqlDb.delete(TABLE_NAME, null, null);
	}
	
	
	public long createTransaction(int amount, String category){
		ContentValues values = new ContentValues();
		values.put(KEY_AMOUNT, amount);
		values.put(KEY_CATEGORY, category);
		Timestamp t = new Timestamp(new Date().getTime());
		values.put(KEY_DATE, t.toString());
		return sqlDb.insert(TABLE_NAME, null, values);
	}
	
	public Cursor fetchAllTransactions(){
		return sqlDb.query(TABLE_NAME,new String[]{KEY_ROW_ID,KEY_AMOUNT,KEY_CATEGORY},null,null,null,null,"date DESC");
	}
	
	public Cursor fetchSingleTransaction(long id){ 
		return sqlDb.query(TABLE_NAME, new String[]{KEY_AMOUNT, KEY_CATEGORY},"_id =?",new String[]{""+id},null,null,null,null);
	}
	
	public void deleteTransaction(long id){
		sqlDb.delete(TABLE_NAME, KEY_ROW_ID + "=" + id, null);
	}
	
	public int editTransaction(long id, int amount, String description){
		ContentValues values = new ContentValues();
		values.put(KEY_AMOUNT, amount);
		values.put(KEY_CATEGORY, description);
		Log.i("update", ""+id +" "+amount+" "+description);
		return sqlDb.update(TABLE_NAME, values, "_id =?", new String[]{""+id});
	}
	
	public int getBalance(){
		Cursor data = sqlDb.query(TABLE_NAME, new String[]{KEY_AMOUNT}, null, null, null, null, null);
		int total = 0;
		if(data.moveToFirst()){
			while(!(data.isLast())){
				total += data.getInt(0);
				data.moveToNext();
			}
			total+=data.getInt(0);
		}
		return total;
	}
	
	public boolean newProfile(String tableName){
		
		return true;
	}
	
	public boolean changeTable(String tableName){
		
		return true;
	}
}
