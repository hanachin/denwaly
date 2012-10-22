package in.hanach.denwaly;

import java.io.IOException;
import java.util.UUID;

import org.apache.http.client.methods.HttpGet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class PhoneStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
	        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	        SharedPreferences.Editor editor = settings.edit();
	        String uuid = UUID.randomUUID().toString();
	        uuid = settings.getString("uuid", uuid);
	        editor.putString("uuid", uuid);
	        editor.commit();
	        
			final String url = "http://" + MainActivity.HOST + "/call/" + uuid;
	        new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						AndroidHttpClient client = AndroidHttpClient.newInstance("Denwaly");
						client.execute(new HttpGet(url));
					} catch (IOException e) {
					}
					return null;
				}

	        }.execute();
		}
	}
}
