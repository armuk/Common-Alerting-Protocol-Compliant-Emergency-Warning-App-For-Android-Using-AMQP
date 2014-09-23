package in.ac.iitd.cse.iuatc.ews;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.locks.Lock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.MicrosoftTranslatorAPI;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class AlertListFragment extends Fragment implements OnItemClickListener,
		TextToSpeech.OnInitListener {

	private static final String TAG = "ALERTLISTFRAG";

	
	private AlertsListAdapter adapter = null;
	private ArrayList<SimpleAlert> alerts = null;
	private ListView listView;
	private TextToSpeech tts;
	private TextView translationPopupTextView;
	private PopupWindow popupWindow;
	private View view;
	private EwsApplication ewsApplication;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.v(TAG,"onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.v(TAG,"onAttach");
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onDestroyView() {
		Log.v(TAG,"onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		Log.v(TAG,"onDetach");
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onStart() {
		Log.v(TAG,"onStart");
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.v(TAG,"onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		Log.v(TAG,"onViewStateRestored");
		super.onViewStateRestored(savedInstanceState);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG,"onCreate");
		super.onCreate(savedInstanceState);
		alerts = new ArrayList<SimpleAlert>();
		this.adapter = new AlertsListAdapter(getActivity(), R.layout.list_row,this.alerts);
		new LoadAlertsFromDatabaseTask().execute();
		ewsApplication = (EwsApplication)(getActivity().getApplicationContext());
		ewsApplication.alertListFragment=this;
	}

	private class LoadAlertsFromDatabaseTask extends AsyncTask<Void, Integer, Void> {
		
		ProgressDialog progressDialog;
		Lock lock;
		
		@Override
		protected void onPreExecute() {
			this.progressDialog = ProgressDialog.show(getActivity(),
					"Loading alerts", "Please Wait", true);
		};

		@Override
		protected Void doInBackground(Void... params) {
			lock = ewsApplication.getLock();
			if(!lock.tryLock()) {
				do {
					try { Thread.sleep(100);} catch (InterruptedException e) { }
				} while (!lock.tryLock());
			}
			Utils.getAlertsFromDataBase(getActivity(),alerts);
			ewsApplication.alertListFragment=AlertListFragment.this;
			lock.unlock();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			this.progressDialog.dismiss();
		}

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG,"onCreateView");
		this.view = inflater.inflate(R.layout.alert_list_fragment, null);
		this.listView = (ListView) this.view.findViewById(R.id.listView1);

		this.listView.setAdapter(this.adapter);
		this.listView.setOnItemClickListener(this);
		registerForContextMenu(this.listView);

		this.listView
				.setEmptyView(this.view.findViewById(R.id.empty_list_item));

		return this.view;
	}

	@Override
	public void onPause() {
		Log.v(TAG,"onPause");
		if (this.tts != null) {
			this.tts.stop();
			this.tts.shutdown();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.v(TAG,"onResume");
		super.onResume();
		this.tts = new TextToSpeech(getActivity(), this);
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.alert_list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		String text = this.adapter.getItem(info.position).getHeadline();
		switch (item.getItemId()) {
		case R.id.tts:
			this.tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			return true;
		case R.id.send_via_sms:
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", text);
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
			return true;
		case R.id.translate_to_hindi:
			translateAndShowPopup(text);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ((MainActivityTest) getActivity()).mIndicator.notifyDataSetChanged();
		Intent showAlert = new Intent(getActivity(), DetailActivity.class);
		showAlert.putExtra("ALERT", this.alerts.get(position));
		startActivity(showAlert);
	}

	@Override
	public void onDestroy() {
		Log.v(TAG,"onDestroy");
		super.onDestroy();
		ewsApplication.alertListFragment=null;
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = this.tts.setLanguage(Locale.US);

			if ((result == TextToSpeech.LANG_MISSING_DATA)
					|| (result == TextToSpeech.LANG_NOT_SUPPORTED)) {
				Log.e("TTS", "This Language is not supported");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	public void translateAndShowPopup(String text) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.translation_popup, null);
		this.translationPopupTextView = (TextView) popupView
				.findViewById(R.id.translated_text);
		this.translationPopupTextView.setText("Translating....");
		this.popupWindow = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
		btnDismiss.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertListFragment.this.popupWindow.dismiss();
			}
		});

		Button btnHindiSpeak = (Button) popupView
				.findViewById(R.id.btnHindiSpeak);
		btnHindiSpeak.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				Locale s[] = Locale.getAvailableLocales();
				int i = 0;
				for (i = 0; i < s.length; i++) {
					if (s[i].getLanguage().equalsIgnoreCase("hi")) {
						break;
					}
				}

				int result = AlertListFragment.this.tts.setLanguage(s[i]);

				if ((result == TextToSpeech.LANG_MISSING_DATA)
						|| (result == TextToSpeech.LANG_NOT_SUPPORTED)) {
					Log.e("TTS", "This Language is not supported");

					Toast.makeText(getActivity()
							.getApplicationContext(),
							"You do not have epeakTTS App installed."
									+ "Please install it from Google Play.",
							Toast.LENGTH_LONG).show();

				} else {

					AlertListFragment.this.tts
							.speak(AlertListFragment.this.translationPopupTextView
									.getText().toString(),
									TextToSpeech.QUEUE_FLUSH, null);
				}

				AlertListFragment.this.tts.setLanguage(Locale.US);

			}
		});

		final TranslateTask translateTask = new TranslateTask();
		translateTask.execute(text);
		android.os.Handler handler = new android.os.Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (translateTask.getStatus() == AsyncTask.Status.RUNNING) {
					translateTask.cancel(true);
				}
			}
		}, 40000);
	}

	public String translate(String text) throws Exception {
		MicrosoftTranslatorAPI.setClientId("IU-ATC");
		MicrosoftTranslatorAPI
				.setClientSecret("lA+jVp79EagWlz69x/qAuz6wZgmf1aDfj23z/tZ25OA=");
		String translatedText = "";
		translatedText = Translate.execute(text, Language.HINDI);
		return translatedText;
	}

	class TranslateTask extends AsyncTask<String, Void, Void> {
		ProgressDialog progressDialog;
		String translatedText = "";
		boolean error = false;

		@Override
		protected void onPreExecute() {
			this.progressDialog = ProgressDialog.show(getActivity(),
					"Translating", "Please Wait", true);

		};

		@Override
		protected Void doInBackground(String... params) {
			try {
				String text = params[0];
				if (!text.equalsIgnoreCase("")) {
					this.translatedText = translate(text);
				} else {
					this.translatedText = "No Input!!!";
				}
			} catch (Exception e) {
				this.translatedText = "No network connection !!";
				this.error = true;
				return null;
			}

			return null;
		}

		@Override
		protected void onCancelled(Void result) {
			this.progressDialog.dismiss();
			Toast.makeText(getActivity().getApplicationContext(),
					"No network connection !!", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			this.progressDialog.dismiss();
			if (this.error) {
				Toast.makeText(getActivity(),
						this.translatedText, Toast.LENGTH_LONG).show();
			} else {
				AlertListFragment.this.translationPopupTextView.setText(this.translatedText);
				AlertListFragment.this.popupWindow.showAtLocation(
						AlertListFragment.this.listView, Gravity.CENTER, 0, 0);
			}
		}

	}

	public void addAlert(SimpleAlert simpleAlert) {
		if(alerts!=null && adapter!=null) {
			alerts.add(0,simpleAlert);
			if(this.isResumed()) {
				adapter.notifyDataSetChanged();
			}
		}
	}

}