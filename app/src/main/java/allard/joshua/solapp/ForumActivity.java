package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends BaseActivity implements AdapterView.OnItemSelectedListener  {

    public static List<String> lines;
    public static List<ThreadTitle> threads;
    public static Document doc;
    public List<Element> links = new ArrayList<>();
    //private boolean finishedLoading = false;
    private int currentPage = -1;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_forum);
        try {
            TextView text = (TextView) findViewById(R.id.textViewMain);
            text.setText("Threads Displayed: " + threads.size());

            ItemScroll threadView = (ItemScroll) findViewById(R.id.mailScroll);
            threadView.populateItemScroll(15, this, threads, false);
            threadView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ThreadActivity.returnToActivity(activity, threads.get(position));
                }
            });

            Elements elements = doc.select("a[href*=&st=]");
            List<String> pages = new ArrayList<>();
            //Log.d("linkcount", links.size() + "");
            for(Element e: elements){
                //Log.d("link", e.toString());
                //Log.d("linktext", "[" + e.text() + "]");
                try {

                    if (Integer.parseInt(e.text().trim()) > 0) {
                        //ThreadTitle threadTitle = new ThreadTitle(e);
                        //threads.add(threadTitle);
                        pages.add("Page: " + e.text());
                        links.add(e);
                        if(e.select("font").size() > 0){
                            currentPage = pages.size() - 1;
                            //throw new Exception();
                        }
                    } else {
                        break;
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
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
        if(position != currentPage) {
            Element selectedPage = links.get(position);
            returnToActivity(this, selectedPage.attr("href"));
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public static void returnToActivity(final Activity activity){
        returnToActivity(activity, "yourgang.php?action=forums");
    }

    public static void returnToActivity(final Activity activity, String url){
        try {
            lines = Connector.loadPage(null, url);
            threads = processThreads(lines);
            //Log.d("Threads", threads.size() + "");
        }
        catch(Exception e){
            Log.d("Debug", e.toString());
        }
        Intent mainIntent = new Intent(activity, ForumActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void refresh(){
        returnToActivity(this);
    }

    private static List<ThreadTitle> processThreads(List<String> htmlLines){
        List<ThreadTitle> threads = new ArrayList<>();
        String page = "";
        for(String s: htmlLines){
            page += s + "\n";
        }
        //Spanned html = Html.fromHtml(page);
        doc = Jsoup.parse(page);
        boolean adding = false;
        Mail currentMail = null;
        for(int i = 0; i < htmlLines.size(); i++){
            if (htmlLines.get(i).startsWith("<tr> <td align='center'>")) {
                try {
                    ThreadTitle thread = new ThreadTitle(htmlLines.get(i++), htmlLines.get(i++), htmlLines.get(i++),
                            htmlLines.get(i));

                    //ThreadTitle thePreviousThread = events.get(thread.getTitle());

                    threads.add(thread);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        return threads;
    }


    @Override
    public void onBackPressed() {
        MainActivity.returnToMainActivity(this);
    }
}
