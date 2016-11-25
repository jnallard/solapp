package allard.joshua.solapp.parser;

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

    public void Parse(Document doc){
        this.doc = doc;
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
