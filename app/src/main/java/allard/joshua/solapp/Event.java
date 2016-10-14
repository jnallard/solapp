package allard.joshua.solapp;

import org.jsoup.nodes.Element;

/**
 * Created by Joshua on 3/14/2016.
 */
public class Event implements HTMLListItem {
    String postNum = "";
    String date = "test";
    String body = "test";

    public Event(Element trElement) {
        trElement.child(0).select("img").remove();
        date = trElement.child(0).toString();
        body = trElement.child(1).toString();
    }

    @Override
    public String getHTML() {
        return "<h1>" + date + "</h1><br />" +
                body;
    }
}
