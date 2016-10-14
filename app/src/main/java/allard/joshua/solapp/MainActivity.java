package allard.joshua.solapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends BaseActivity {

    public static List<String> lines;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        try {
            TextView text = (TextView) findViewById(R.id.textViewMain);
            //text.setText(lines.size() + "");
            String page = "";
            for(String s: lines){
                page += s + "\n";
            }
            //Spanned html = Html.fromHtml(page);
            Document doc = Jsoup.parse(page);
            Elements e = doc.select("font");
            Element infoE = e.get(0);

            TextView mainInfo = (TextView) findViewById(R.id.mainInfo);
            mainInfo.setText(Html.fromHtml(infoE.html()));

            mainInfo.setMovementMethod(CustomLinkMovementMethod.getInstance(this));

        }
        catch(Exception e){
            Log.d("Debug", "Failed: " + e.getMessage());
        }
        //setContentView(R.layout.activity_main);
    }

    public static void returnToMainActivity(final Activity activity){
        try {
            lines = Connector.loadPage(null, "index.php");
        }
        catch(Exception e){
            Log.d("Debug", e.getMessage());
        }
        Intent mainIntent = new Intent(activity, MainActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void refresh(){
        returnToMainActivity(this);
    }
}
