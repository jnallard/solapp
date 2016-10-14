package allard.joshua.solapp;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by Joshua on 11/3/2015.
 */
public class SiteStatus {
    public String mails = "?";
    public String events = "?";
    public String id = "";
    public String user = "";
    private Document doc;

    public SiteStatus(List<String> lines){

        String page = "";
        for(String s: lines){
            page += s + "\n";
        }
        //Spanned html = Html.fromHtml(page);
        doc = Jsoup.parse(page);

        Elements top = doc.select("font[color=white]");
        Log.d("top", top.toString());

        if(top.size() > 0) {
            String nameString = top.get(0).toString();
            Log.d("name", nameString);
            int start = nameString.indexOf("[");
            int end = nameString.indexOf("]");
            if (start != -1 && end != -1)
                id = nameString.substring(start + 1, end);
            Log.d("id", id);
        }

        for(int i = 0; i < lines.size(); i++){
            String current = lines.get(i);
            /*if(current.contains("<b>Name:</b>") && id.length() > 0){
                id = current.substring(current.indexOf("<b>Name:</b>") + 12, current.indexOf("] <br />"));
                Log.d("id", id);
                user = id.substring(0, id.indexOf("["));
                id = id.substring(id.indexOf("[") + 1);
                Log.d("Debug", "-" + user + "-" + id + "-");
            }*/
            if(current.contains("<!-- Links -->")) {
                processForEventsAndMails(lines.get(i + 1));
            }
        }
    }

    private void processForEventsAndMails(String line){
        String[] links = line.split("</a>");
        for(int i = 0; i < links.length; i++){
            String link = links[i];
            if (link.contains("events.php")){
                String number = link.substring(link.indexOf("(") + 1, link.indexOf(")"));
                //Log.d("Debug", link + " - " + number);
                events = number;
            }
            if (link.contains("mailbox.php")){
                String number = link.substring(link.indexOf("(") + 1, link.indexOf(")"));
                //Log.d("Debug", link + " - " + number);
                mails = number;
            }
        }
    }
}
