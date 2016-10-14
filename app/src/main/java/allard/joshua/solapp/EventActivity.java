package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends BaseActivity {

    public static List<String> lines;
    public static List<Event> events;
    public static Document doc;
    //private boolean finishedLoading = false;
    private int currentPage = -1;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private static String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_mailbox);
        try {
            setTitle(mTitle);
            TextView text = (TextView) findViewById(R.id.textViewMain);
            text.setText("Posts Displayed: " + events.size());

            ItemScroll mailView = (ItemScroll) findViewById(R.id.mailScroll);
            mailView.populateItemScroll(15, this, events, true);

            //finishedLoading = true;

            //int postCount = Integer.parseInt(events.get(events.size() - 1).postNum.split("\\#")[1]);
        }
        catch(Exception e){
            Log.d("Debug", "Failed: " + e.getMessage());
        }
        //setContentView(R.layout.activity_main);
    }

    public static void returnToActivity(final Activity activity){
        try {
            lines = Connector.loadPage(null, "events.php");
            events = processEvents(lines);
        }
        catch(Exception e){
            Log.d("Debug", e.toString());
        }
        Intent mainIntent = new Intent(activity, EventActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void refresh(){
        returnToActivity(this);
    }

    private static List<Event> processEvents(List<String> htmlLines){
        List<Event> events = new ArrayList<>();
        String page = "";
        for(String s: htmlLines){
            page += s + "\n";
        }
        //Spanned html = Html.fromHtml(page);
        doc = Jsoup.parse(page);
        Elements form = doc.select("form[action=\"bevent.php\"");
        //System.out.println(form);
        Elements trs = form.select("tr");
        for(Element e: trs){
            if(e.select("td").size() == 3){
                events.add(new Event(e));
            }
        }

        return events;
    }

    @Override
    public void onBackPressed() {
        ForumActivity.returnToActivity(this);
    }
}
