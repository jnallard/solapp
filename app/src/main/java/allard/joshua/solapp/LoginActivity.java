package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static String message = null;

    EditText username1;
    EditText password1;
    EditText username2;
    EditText password2;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TabHost mTabHost = (TabHost)findViewById(R.id.tabhost);
        mTabHost.setup();

        TabHost.TabSpec tab1 = mTabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("Second Tab");

        tab1.setIndicator("World 1");
        tab1.setContent(R.id.tab1);

        tab2.setIndicator("World 2");
        tab2.setContent(R.id.tab2);

        mTabHost.addTab(tab1);
        mTabHost.addTab(tab2);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username1 = (EditText) findViewById(R.id.username1);
        username1.setText(prefs.getString("usernameKey", ""));
        password1 = (EditText) findViewById(R.id.password1);
        password1.setText(prefs.getString("passwordKey", ""));
        username2 = (EditText) findViewById(R.id.username2);
        username2.setText(prefs.getString("username2Key", ""));
        password2 = (EditText) findViewById(R.id.password2);
        password2.setText(prefs.getString("password2Key", ""));

        loadingBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        if(message != null){
            Log.d("test", message);
            Toast.makeText(this, message,
                    Toast.LENGTH_LONG).show();
            message = null;
        }
    }

    public void connectToSite1(View view){
        NotificationEventReceiver.setupAlarm(getApplicationContext());
        loadingBar.setVisibility(View.VISIBLE);

        String usernameText = username1.getText().toString().trim();
        String passwordText = password1.getText().toString().trim();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("usernameKey", usernameText).apply();
        prefs.edit().putString("passwordKey", passwordText).apply();
        InternetActivity.TITLE = "SoL Mobile - World 1";
        Connector.BaseUrl = "http://www.samuraioflegend.com";
        Connector.WorldSelect = "World+1";
        Connector.login(this, usernameText, passwordText);
    }

    public void connectToSite2(View view){
        NotificationEventReceiver.setupAlarm(getApplicationContext());
        loadingBar.setVisibility(View.VISIBLE);

        String usernameText = username2.getText().toString().trim();
        String passwordText = password2.getText().toString().trim();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("username2Key", usernameText).apply();
        prefs.edit().putString("password2Key", passwordText).apply();
        InternetActivity.TITLE = "SoL Mobile - World 2";
        Connector.BaseUrl = "http://world2.samuraioflegend.com";
        Connector.WorldSelect = "World+2";
        Connector.login(this, usernameText, passwordText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public static void returnToActivity(final Activity activity, String newMessage){
        message = newMessage;
        Intent mainIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }
}
