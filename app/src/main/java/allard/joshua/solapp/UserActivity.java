package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends BaseActivity {

    public static List<String> lines;
    private Document doc;
    private List<Comment> comments = new ArrayList<>();

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_user);
        try {
            setupTabHost();
            EditText idField = (EditText) findViewById(R.id.editText4);
            idField.setText(id);

            String page = "";
            for(String s: lines){
                page += s + "\n";
            }
            //Spanned html = Html.fromHtml(page);
            doc = Jsoup.parse(page);
            String detailsString = "";
            Elements center = doc.select("td:containsOwn(User Level:)");
            detailsString += "<br />" + center.toString() + "<br />";
            center = doc.select("td:containsOwn(Married:)");
            detailsString += "<br />" + center.toString() + "<br />";
            center = doc.select("td:containsOwn(Referals:)");
            detailsString += "<br />" + center.toString() + "<br />";
            center = doc.select("fieldset");
            detailsString += "<br />" + center.toString() + "<br />";


            TextView details = (TextView) findViewById(R.id.userDetails);
            details.setText(Html.fromHtml(detailsString));
            details.setMovementMethod(CustomLinkMovementMethod.getInstance(this));

            center = doc.select("center");
            Elements cmts = center.select("table[width=90%]");
            cmts = cmts.select("tr");
            //System.out.println(cmts.toString());
            for(Element e: cmts){
                if(e.select("td").size() == 3){
                    comments.add(new Comment(e));
                }
            }
            //System.out.println(cmts.toString());



            ItemScroll mailView = (ItemScroll) findViewById(R.id.mailScroll);
            mailView.populateItemScroll(15, this, comments, true);
        }
        catch(Exception e){
            Log.d("Debug", "Failed: " + e.getMessage());
        }
        //setContentView(R.layout.activity_main);
    }

    private void setupTabHost() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("details");
        tabSpec.setContent(R.id.tabDetails);
        tabSpec.setIndicator("Details");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("comments");
        tabSpec.setContent(R.id.tabComments);
        tabSpec.setIndicator("Comments");
        tabHost.addTab(tabSpec);

    }

    public static void returnToActivity(final Activity activity, String id){
        UserActivity.id = id;
        try {
            lines = Connector.loadPage(null, "viewuser.php?u=" + id);
        }
        catch(Exception e){
            Log.d("Debug", e.toString());
        }
        Intent mainIntent = new Intent(activity, UserActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void refresh(){
        returnToActivity(this, id);
    }




    @Override
    public void onBackPressed() {
        MainActivity.returnToMainActivity(this);
    }

    public void onGoClicked(View v){
        EditText idField = (EditText) findViewById(R.id.editText4);
        returnToActivity(this, idField.getText().toString().trim());
    }




    public void postReply(View view){
        try {
            EditText text = (EditText) findViewById(R.id.editText5);
            //String subject = URLEncoder.encode("Sent by SoLApp", "utf-8");
            String message = URLEncoder.encode(text.getText().toString(), "utf-8");
            String submit = URLEncoder.encode("Post Comment", "utf-8");

            String post = "comment=" + message + "&zsubmit=" + submit;
            //Log.d("Debug", post);
            Connector.loadPage(post, "viewuser.php?u=" + id);
            returnToActivity(this, id);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
