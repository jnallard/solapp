package allard.joshua.solapp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 10/14/2016.
 */

public class GreenTemplateParser extends BaseTemplateParser {

    @Override
    public Element GetContent() {
        return doc.select("div[class=\"content\"]").first();
    }

    @Override
    public Elements GetLinks() {
        return doc.select("div[class=\"box\"]").select("a");
    }

    @Override
    public String GetBGColor() {
        return "background-color: #172101;";
    }

    @Override
    public String GetTextColor() {
        return "white";
    }

    @Override
    public String GetLinkColor() {
        return "#ffdd00";
    }

    @Override
    public String GetStatus() {
        String stats1 = getFormattedHtml(doc.select("td[width=\"230\"]").first());
        String stats2 = getFormattedHtml(doc.select("td[class=\"player_stats_items\"]").first());
        return stats1 + "<br />" + stats2;
    }
}
