package allard.joshua.solapp.parser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by Joshua on 10/14/2016.
 */

public class PageParser {
    protected Document doc = null;

    BaseTemplateParser blueParser = new BlueTemplateParser();
    BaseTemplateParser greenParser = new GreenTemplateParser();
    BaseTemplateParser laternParser = new LaternTemplateParser();
    BaseTemplateParser redParser = new RedTemplateParser();
    BaseTemplateParser blackParser = new BlackTemplateParser();


    BaseTemplateParser templateParser = blueParser;

    private PageParser(){

    }

    private static PageParser instance = new PageParser();

    public static PageParser GetInstance(){
        return instance;
    }

    public PageParser Parse(List<String> lines){
        doc = GetDocument(lines);
        Templates newTemplate = GetTemplate();
        Log.d("css", newTemplate.toString());
        switch(newTemplate){
            case BLUE:
            default:
                templateParser = blueParser;
                break;
            case GREEN:
                templateParser = greenParser;
                break;
            case LATERNS:
                templateParser = laternParser;
                break;
            case RED:
                templateParser = redParser;
                break;
            case BLACK:
                templateParser = blackParser;
                break;
        }
        templateParser.Parse(doc);
        return this;
    }

    public Document GetDocument(List<String> lines){
        String page = "";
        for(String s: lines){
            page += s + "\n";
        }
        //Spanned html = Html.fromHtml(page);
        return Jsoup.parse(page);
    }



    private Templates GetTemplate(){
        String css = doc.select("link[rel=\"stylesheet\"]").attr("href");
        Log.d("css", "[" + css + "]");
        switch(css){
            default:
                return Templates.BLUE;
            case "/halloween/style_hallo.css":
            case "style_alogin.css":
                return Templates.GREEN;
            case "http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700,600":
            case "stylebrown.css":
            case "lantern/Lanterns.css":
                return Templates.LATERNS;
            case "style_red.css":
                return Templates.RED;
            case "style3.css":
                return Templates.BLACK;
        }
    }

    private enum Templates {
        BLUE, GREEN, LATERNS, RED, BLACK
    }


    public static BaseTemplateParser GetTemplateInfo(){
        return GetInstance().templateParser;
    }
}
