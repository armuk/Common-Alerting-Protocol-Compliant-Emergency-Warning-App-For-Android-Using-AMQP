package in.ac.iitd.cse.iuatc.ews;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class EwsApplication extends Application {
	AlertListFragment alertListFragment = null;
	private DataBaseHandler dataBaseHandler;
	private Lock lock;

	private static final String TAG = "EWSAPP";
	
	@Override
	public void onCreate() {
		Log.v(TAG,"onCreate");
		super.onCreate();
		dataBaseHandler = new DataBaseHandler(this);
		lock=new ReentrantLock();
		startService(new Intent(getApplicationContext(),
				MiddlewareListencerService.class));

	}
	
	public DataBaseHandler getDataBaseHandler() {
		return dataBaseHandler;
	}

	public Lock getLock() {
		return lock;
	}


}