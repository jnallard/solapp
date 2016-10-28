package allard.joshua.solapp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 10/14/2016.
 */

public class LaternTemplateParser extends BaseTemplateParser {

    @Override
    public Element GetContent() {
        return doc.select("center").parents().first();
    }

    @Override
    public Elements GetLinks() {
        return doc.select("div[class=\"leftnavlinks\"]").select("a");
    }
}
