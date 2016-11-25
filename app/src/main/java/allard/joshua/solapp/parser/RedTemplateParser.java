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
}
