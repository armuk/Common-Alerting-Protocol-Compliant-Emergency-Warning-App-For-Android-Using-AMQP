package in.ac.iitd.cse.iuatc.ews;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

import oasis.names.tc.emergency.cap._1.Alert;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlertReceivedCallback implements IRabbitMQConsumerCallback {

	private static final String TAG = "Rabbit";

	private Context context;
	private EwsApplication ewsApplication;

	public AlertReceivedCallback(Context context) {
		this.context = context;
		this.ewsApplication = (EwsApplication)context.getApplicationContext();
	}

	@Override
	public void messageReceived(byte[] bytes) {
		try {
			Alert cap =(Alert)Utils.deserialize(bytes);
			handleCAPMessage(cap);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void handleCAPMessage(Alert cap) {
		
		SimpleAlert simpleAlert = new SimpleAlert(cap);

		Lock lock = ewsApplication.getLock();		
		if(!lock.tryLock()) {
			do {
				try { Thread.sleep(100);} catch (InterruptedException e) { }
			} while (!lock.tryLock());
		}
				
		try {
			ewsApplication.getDataBaseHandler().insert(Utils.serialize(simpleAlert));
		} catch (IOException e) {Log.e(TAG, "insertion to database failed", e);}

		if (ewsApplication.alertListFragment != null) {
			ewsApplication.alertListFragment.addAlert(simpleAlert);
		}		
		lock.unlock();
		
		createNotification("EWS Notification", "Warning from "
				+ cap.getSender());
		
	}

	private void createNotification(String contentTitle, String contentText) {
		long[] vibrationPattern = { 0, 1000, 500, 1000, 500, 1000 };
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setContentTitle(contentTitle).setContentText(contentText)
				.setVibrate(vibrationPattern).setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher);

		Uri audioURI = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(audioURI);

		
		Intent resultIntent = new Intent(context, MainActivity.class);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


		mBuilder.setContentIntent(resultPendingIntent);

		int mNotificationId = 001;
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}
}
