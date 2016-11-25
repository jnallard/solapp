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
}
