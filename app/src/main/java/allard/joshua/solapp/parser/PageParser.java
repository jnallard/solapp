package allard.joshua.solapp.parser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by Joshua on 10/14/2016.
 */

public class PageParser {
    private Document doc = null;

    private BaseTemplateParser blueParser = new BlueTemplateParser();
    private BaseTemplateParser whiteParser = new WhiteTemplateParser();
    private BaseTemplateParser brownParser = new BrownTemplateParser();
    private BaseTemplateParser orangeParser = new OrangeTemplateParser();
    private BaseTemplateParser greenParser = new GreenTemplateParser();
    private BaseTemplateParser laternParser = new LaternTemplateParser();
    private BaseTemplateParser redParser = new RedTemplateParser();
    private BaseTemplateParser blackParser = new BlackTemplateParser();


    private BaseTemplateParser templateParser = blueParser;

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
            case WHITE:
                templateParser = whiteParser;
                break;
            case BROWN:
                templateParser = brownParser;
                break;
            case ORANGE:
                templateParser = orangeParser;
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
            case LOGGED_OUT:
                templateParser = null;
                return this;
        }
        templateParser.Parse(doc);
        return this;
    }

    public Document GetDocument(List<String> lines){
        String page = "";
        for(String s: lines){
            page += s + "\n";
        }
        return Jsoup.parse(page);
    }



    private Templates GetTemplate(){
        String css = doc.select("link[rel=\"stylesheet\"]").attr("href");
        Log.d("css", "[" + css + "]");
        switch(css){
            default:
                return Templates.BLUE;
            case "":
                return Templates.WHITE;
            case "/halloween/style_hallo.css":
            case "style_alogin.css":
                return Templates.GREEN;
            case "stylebrown.css":
                return Templates.BROWN;
            case "http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700,600":
                return Templates.ORANGE;
            case "lantern/Lanterns.css":
                return Templates.LATERNS;
            case "style_red.css":
            case "style_red2.css":
                return Templates.RED;
            case "style3.css":
                return Templates.BLACK;
            case "stylesfb.css":
                return Templates.LOGGED_OUT;
        }
    }

    private enum Templates {
        BLUE, WHITE, GREEN, LATERNS, RED, BLACK, LOGGED_OUT, BROWN, ORANGE
    }


    public static BaseTemplateParser GetTemplateInfo(){
        return GetInstance().templateParser;
    }
}
