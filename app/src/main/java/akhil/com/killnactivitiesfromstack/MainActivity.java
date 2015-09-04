package akhil.com.killnactivitiesfromstack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static final String ACTIVITY_POSITION = "Activity Position";
    private static final String KILL_ACTION = "kill_action";
    private int mCurrentActivityPosition = 0;

    private BroadcastReceiver mKillBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra(ACTIVITY_POSITION)) {
                if (intent.getIntExtra(ACTIVITY_POSITION, Integer.MIN_VALUE) > mCurrentActivityPosition) {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ACTIVITY_POSITION)) {
            mCurrentActivityPosition = intent.getIntExtra(ACTIVITY_POSITION, mCurrentActivityPosition) + 1;
        }
        ((TextView) findViewById(R.id.tv_activity_number)).setText("On Activity Number : " + mCurrentActivityPosition);
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                intent1.putExtra(ACTIVITY_POSITION, mCurrentActivityPosition);
                startActivity(intent1);
            }
        });
        findViewById(R.id.btn_kill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextValue = ((EditText) findViewById(R.id.et_no_of_kill)).getText().toString();

                try {
                    int killPageNo = Integer.parseInt(editTextValue);
                    Intent killIntent = new Intent(KILL_ACTION);
                    killIntent.putExtra(ACTIVITY_POSITION, killPageNo);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(killIntent);
                } catch (NumberFormatException nfe) {
                    Toast.makeText(getApplicationContext(), "Not a integer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mKillBroadCastReceiver, new IntentFilter(KILL_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mKillBroadCastReceiver);
    }
}
