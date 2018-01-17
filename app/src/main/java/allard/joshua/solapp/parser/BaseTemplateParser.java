package allard.joshua.solapp.parser;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 10/14/2016.
 */

public abstract class BaseTemplateParser {
    protected Document doc = null;
    public String Mails = "";
    public String Events = "";

    int mailCount = 0;
    int eventCount = 0;
    int announcementCount = 0;

    public void Parse(Document doc){
        this.doc = doc;

        mailCount = 0;
        eventCount = 0;
        announcementCount = 0;

        Elements links = GetLinks();

        for (Element e : links) {
            String text = e.text();
            if(text.startsWith("You Have Mail")){
                mailCount = Integer.parseInt(text.substring(text.indexOf("(") + 1, text.indexOf(")")));
                Log.d("Mails", mailCount + "");
            }

            if(text.startsWith("Events") && text.contains("(")){
                eventCount = Integer.parseInt(text.substring(text.indexOf("(") + 1, text.indexOf(")")));
                Log.d("Events", eventCount + "");
            }

            if(text.startsWith("Announcements") && text.contains("(")){
                announcementCount = Integer.parseInt(text.substring(text.indexOf("(") + 1, text.indexOf(")")));
                Log.d("Announcements", announcementCount + "");
            }
        }
    }


    public Elements GetLinks(){
        return doc.select("td[width=\"260\"]").select("a");
    }

    public Element GetContent(){
        return doc.select("td[width=\"83%\"]").first();
    }

    public String GetStatus(){
        String stats1 = getFormattedHtml(doc.select("td[width=\"220\"]").first());
        String stats2 = getFormattedHtml(doc.select("td[width=\"262\"]").first());
        return stats1 + "<br />" + stats2;
    }

    public Element GetNoLinksContent(){
        return doc.select("center").first();
    }

    public String GetBGColor() { return "background-color: #3C6B99;"; }

    public String GetTextColor() { return "black"; }

    public String GetLinkColor() { return "white"; }

    public int GetMailCount() {
        return this.mailCount;
    }

    public int GetEventCount() {
        return this.eventCount;
    }

    public int GetAnnouncementCount() {
        return this.announcementCount;
    }

    public Element GetLinkWithText(String text) {
        return GetLinks().select(String.format("a:contains(%s)", text)).first();
    }

    protected String getFormattedHtml(Element element){
        if(element == null){
            return "No content found";
        }

        for( Element e : element.getAllElements() )
        {
            if(e.tagName().equalsIgnoreCase("a")){
                e.remove();
            }
            if(e.tagName().equalsIgnoreCase("font")){
                e.removeAttr("color");
            }
        }

        return element.html();
    }
}
