package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ThreadActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    public static List<String> lines;
    public static List<Post> posts;
    public static ThreadTitle thread;
    public static Document doc;
    public List<ThreadTitle> threads = new ArrayList<>();
    //private boolean finishedLoading = false;
    private int currentPage = -1;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private static String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_thread);
        try {
            setTitle(mTitle);
            TextView text = (TextView) findViewById(R.id.textViewMain);
            text.setText("Posts Displayed: " + posts.size());

            ItemScroll mailView = (ItemScroll) findViewById(R.id.mailScroll);
            mailView.populateItemScroll(15, this, posts, true);

            Elements links = doc.select("a[href*=&st=]");
            List<String> pages = new ArrayList<>();
            for(Element e: links){
                //Log.d("link", e.toString());
                try {
                    if (Integer.parseInt(e.text()) > 0) {
                        ThreadTitle threadTitle = new ThreadTitle(e);
                        threads.add(threadTitle);
                        pages.add("Page: " + threadTitle.pageNum);
                        if(e.select("font").size() > 0){
                            currentPage = pages.size() - 1;
                        }
                    } else {
                        break;
                    }
                }
                catch (Exception ex){
                    break;
                }
            }
            //Log.d("count", pages.size() + "");

            Spinner spinner = (Spinner) findViewById(R.id.pageSelect);

            // Spinner click listener
            spinner.setOnItemSelectedListener(this);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pages);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
            spinner.setSelection(currentPage);

            if(doc.select("i:containsOwn(This topic has been locked, you cannot reply to it.)").size() > 0){
                Button postButton = (Button) findViewById(R.id.PostMessage);
                postButton.setEnabled(false);
                postButton.setText("This Thread is Locked.");
            }

            //finishedLoading = true;

            //int postCount = Integer.parseInt(events.get(events.size() - 1).postNum.split("\\#")[1]);
        }
        catch(Exception e){
            Log.d("Debug", "Failed: " + e.getMessage());
        }
        //setContentView(R.layout.activity_main);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        if(position != currentPage && currentPage != -1) {
            ThreadTitle selectedPage = threads.get(position);
            returnToActivity(this, selectedPage);
        }
        else{
            currentPage = -2;
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public static void returnToActivity(final Activity activity, ThreadTitle thread){
        ThreadActivity.thread = thread;
        try {
            lines = Connector.loadPage(null, thread.url);
            posts = processPosts(lines);
            mTitle = thread.getTitle();
            //Log.d("Threads", posts.size() + "");
        }
        catch(Exception e){
            Log.d("Debug", e.toString());
        }
        Intent mainIntent = new Intent(activity, ThreadActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void refresh(){
        returnToActivity(this, thread);
    }

    private static List<Post> processPosts(List<String> htmlLines){
        List<Post> posts = new ArrayList<>();
        String page = "";
        for(String s: htmlLines){
            page += s + "\n";
        }
        //Spanned html = Html.fromHtml(page);
        doc = Jsoup.parse(page);
        //doc.toString();
        //System.out.println(doc.toString());
        Elements rows = doc.select("tr");
        for(int i = 0; i < rows.size(); i++){
            //System.out.println(e.tagName());
            try {

                if(rows.get(i).child(0).tagName() == "th"){
                    Post post = new Post(rows.get(i), rows.get(++i));
                    posts.add(post);
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        List<ThreadTitle> threads = new ArrayList<>();
        boolean adding = false;
        Mail currentMail = null;
        for(int i = 0; i < htmlLines.size(); i++){
        }

        return posts;
    }


    public void postReply(View view){
        try {
            EditText text = (EditText) findViewById(R.id.editText5);
            String subject = URLEncoder.encode("Sent by SoLApp", "utf-8");
            String message = URLEncoder.encode(text.getText().toString(), "utf-8");
            String post = "fp_subject=" + subject + "&fp_text=" + message;
            //Log.d("Debug", post);
            Connector.loadPage(post, "forums.php?reply=" + thread.topicNum);
            returnToActivity(this, thread);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        ForumActivity.returnToActivity(this);
    }
}
