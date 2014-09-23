package in.ac.iitd.cse.iuatc.ews;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertDetailsAdapter extends ArrayAdapter<StringPair> {

	private ArrayList<StringPair> alertDetails;
	int ViewResourceId;

	public AlertDetailsAdapter(Context context, int textViewResourceId,
			ArrayList<StringPair> alertDetails) {
		super(context, textViewResourceId, alertDetails);
		this.alertDetails = alertDetails;
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
			holder.heading = (TextView) v.findViewById(R.id.heading);
			holder.info = (TextView) v.findViewById(R.id.info);
			v.setTag(holder);
		}
		StringPair info = this.alertDetails.get(position);

		holder = (ViewHolder) v.getTag();

		holder.heading.setText(info.first);
		holder.info.setText(info.second);

		return v;
	}

	private static class ViewHolder {
		public TextView heading;
		public TextView info;
	};

}