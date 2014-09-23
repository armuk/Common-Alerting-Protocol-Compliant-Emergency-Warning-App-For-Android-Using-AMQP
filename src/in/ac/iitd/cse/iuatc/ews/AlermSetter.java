package in.ac.iitd.cse.iuatc.ews;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlermSetter {
	private static final long intervalInMillis = 10 * 1000;
	public static void setAlerm(Context context) {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SendLocationUpdateService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), intervalInMillis, pi);
        Log.d("SendLocationUpdateService","Alerm set");
	}
}
