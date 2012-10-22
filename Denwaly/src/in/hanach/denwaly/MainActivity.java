package in.hanach.denwaly;

import java.util.UUID;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String HOST = "denwaly.jit.su";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.share_button);
        button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String uuid = uuid();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, "http://" + HOST + "/wait/" + uuid);
				startActivity(intent);
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    String uuid() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        String uuid = UUID.randomUUID().toString();
        uuid = settings.getString("uuid", uuid);
        editor.putString("uuid", uuid);
        editor.commit();
        return uuid;
    }
}
