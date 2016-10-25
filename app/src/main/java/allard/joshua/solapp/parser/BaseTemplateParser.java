package allard.joshua.solapp.parser;

import android.os.Debug;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    public void Parse(List<String> lines){
        doc = GetDocument(lines);

        UpdateUser();

        UpdateID();

        UpdateMails();

        UpdateEvents();
    }

    protected void UpdateUser(){
        Elements e = doc.select("font");
        String text = e.first().text();
        User = text.substring(5, text.indexOf("Money:"));

        Log.d("User", User);
    }

    protected void UpdateID(){
        ID = User.substring(User.lastIndexOf('[') + 1, User.lastIndexOf(']'));

        Log.d("ID", ID);
    }

    protected void UpdateMails(){
        Elements e = doc.select(":containsOwn(Mail)");
        Mails = e.first().text();
        Log.d("Mails", Mails);
    }

    protected void UpdateEvents(){
        Elements e = doc.select(":containsOwn(Events)");
        Events = e.first().text();
        Log.d("Events", Events);
    }

    protected Document GetDocument(List<String> lines){
        String page = "";
        for(String s: lines){
            page += s + "\n";
        }
        //Spanned html = Html.fromHtml(page);
        return Jsoup.parse(page);
    }



}
