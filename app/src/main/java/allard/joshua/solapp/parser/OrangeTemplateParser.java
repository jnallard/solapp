package allard.joshua.solapp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 10/14/2016.
 */

public class OrangeTemplateParser extends BaseTemplateParser {

    @Override
    public Element GetContent() {
        return doc.select("center").parents().first();
    }

    @Override
    public Elements GetLinks() {
        return doc.select("div[class=\"leftnavlinks\"]").select("a");
    }

    @Override
    public String GetBGColor() {
        return "background-color: #000000;";
    }

    @Override
    public String GetLinkColor() {
        return "#ff7800";
    }

    @Override
    public String GetTextColor() {
        return "white";
    }

    @Override
    public String GetStatus() {
        String stats1 = getFormattedHtml(doc.select("td[width=\"250px\"]").first());
        String stats2 = getFormattedHtml(doc.select("td[width=\"200\"]").first());
        return stats1 + "<br />" + stats2;
    }
}
