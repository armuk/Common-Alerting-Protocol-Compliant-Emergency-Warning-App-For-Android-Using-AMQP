package in.ac.iitd.cse.iuatc.ews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "alerts.db";
	
    static final String TABLE_CAP = "captable";
    static final String KEY_ID = "_id";
    static final String KEY_CAP_BLOB = "cap";
    
    private static final String TABLE_CREATE =
                "CREATE TABLE " +TABLE_CAP + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_CAP_BLOB + " BLOB UNIQUE NOT NULL UNIQUE);";

    
	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public void insert(byte[] bytes) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CAP_BLOB, bytes );
		db.insert(TABLE_CAP, null, values);
		db.close(); 
	}
	
	public Cursor getAll() {
		 return this.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_CAP, null);
	}
}
