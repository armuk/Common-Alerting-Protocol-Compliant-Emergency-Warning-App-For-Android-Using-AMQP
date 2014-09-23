package in.ac.iitd.cse.iuatc.ews;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;



public class Utils {
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
    
    public static void getAlertsFromDataBase(Context context, ArrayList<SimpleAlert> alerts) {
    	EwsApplication app = (EwsApplication)context.getApplicationContext();
    	Cursor cursor = app.getDataBaseHandler().getAll();
    	if (cursor.moveToFirst()) {
    		do {
    		    try {
					alerts.add(0,
							(SimpleAlert)deserialize(
									cursor.getBlob(
											cursor.getColumnIndex(DataBaseHandler.KEY_CAP_BLOB))));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		} while (cursor.moveToNext());
    	}    	
    }
}
