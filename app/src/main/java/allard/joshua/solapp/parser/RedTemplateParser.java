package allard.joshua.solapp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 10/14/2016.
 */

public class RedTemplateParser extends GreenTemplateParser {

    @Override
    public String GetBGColor() {
        return "background-image: url(bg.png);";
    }

    @Override
    public String GetTextColor() {
        return "white";
    }

    @Override
    public String GetLinkColor() {
        return "#ff7800";
    }

    @Override
    public String GetStatus() {
        String stats1 = getFormattedHtml(doc.select("td[width=\"237\"]").first());
        String stats2 = getFormattedHtml(doc.select("td[class=\"player_stats_items\"]").first());
        return stats1 + "<br />" + stats2;
    }
}
