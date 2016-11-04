package allard.joshua.solapp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 10/14/2016.
 */

public class BlackTemplateParser extends BaseTemplateParser {

    @Override
    public Element GetContent() {
        return doc.select("td[class=\"tiny9\"]").first();
    }

    @Override
    public Elements GetLinks() {
        return doc.select("td[class=\"tiny4\"]").select("a");
    }

    @Override
    public Element GetNoLinksContent(){
        return doc.select("center").get(1);
    }
}
