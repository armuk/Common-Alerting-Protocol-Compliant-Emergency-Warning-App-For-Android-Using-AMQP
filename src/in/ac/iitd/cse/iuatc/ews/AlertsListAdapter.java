package in.ac.iitd.cse.iuatc.ews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertsListAdapter extends ArrayAdapter<SimpleAlert> {

	private ArrayList<SimpleAlert> alerts;

	public ArrayList<SimpleAlert> getAlerts() {
		return this.alerts;
	}

	public void setAlerts(ArrayList<SimpleAlert> alerts) {
		this.alerts = alerts;
	}

	int ViewResourceId;

	public AlertsListAdapter(Context context, int textViewResourceId,
			ArrayList<SimpleAlert> alerts) {
		super(context, textViewResourceId, alerts);
		this.alerts = alerts;
		this.ViewResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(this.ViewResourceId, null);
			holder = new ViewHolder();
			holder.headline = (TextView) v.findViewById(R.id.headline);
			holder.datetime = (TextView) v.findViewById(R.id.datetime);
			v.setTag(holder);
		}

		SimpleAlert alert = this.alerts.get(position);

		holder = (ViewHolder) v.getTag();

		holder.headline.setText(alert.getHeadline());
		holder.datetime.setText(new SimpleDateFormat("dd MMM hh:mm a",
				Locale.ENGLISH).format(alert.getIssueDate()));

		return v;

	}

	private static class ViewHolder {
		public TextView headline;
		public TextView datetime;
	};

}