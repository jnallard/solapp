package allard.joshua.solapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class LoginActivity extends AppCompatActivity {



    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = (EditText) findViewById(R.id.editText);
        username.setText(prefs.getString("usernameKey", ""));
        password = (EditText) findViewById(R.id.editText2);
        password.setText(prefs.getString("passwordKey", ""));
    }

    public void connectToSite(View view){

        String usernameText = username.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("usernameKey", usernameText).apply();
        prefs.edit().putString("passwordKey", passwordText).apply();
        //Log.d("Debug", "-1");
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

    public static void returnToActivity(final Activity activity){
        Intent mainIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }
}
