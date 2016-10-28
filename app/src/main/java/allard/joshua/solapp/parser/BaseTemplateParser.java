package allard.joshua.solapp.parser;

import android.os.Debug;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.util.List;

/**
 * Created by Joshua on 10/14/2016.
 */

public abstract class BaseTemplateParser {
    protected Document doc = null;
    public String User = "";
    public String ID = "";
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
}
