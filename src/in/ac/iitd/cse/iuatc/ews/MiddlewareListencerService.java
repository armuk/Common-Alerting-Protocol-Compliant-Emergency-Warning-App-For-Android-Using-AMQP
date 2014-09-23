package in.ac.iitd.cse.iuatc.ews;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MiddlewareListencerService extends Service {


	private static final String TAG="MWSERVICE";
		
	private RabbitMQConsumer rabbitConsumer;

	@Override
	public void onCreate() {
		Log.v(TAG,"onCreate");
		this.rabbitConsumer = new RabbitMQConsumer(Installation.id(this), new AlertReceivedCallback(this));
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG,"onStartCommand");
		this.rabbitConsumer.start();
		AlermSetter.setAlerm(this);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.v(TAG,"onDestroy");
		super.onDestroy();
		this.rabbitConsumer.stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
