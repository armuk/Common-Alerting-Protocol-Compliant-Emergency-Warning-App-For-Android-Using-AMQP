package in.ac.iitd.cse.iuatc.ews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myIntent = new Intent(context, MiddlewareListencerService.class);
		context.startService(myIntent);
	}
}
