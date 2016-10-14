package allard.joshua.solapp;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 3/14/2016.
 */
public class Comment implements HTMLListItem {
    String postNum = "";
    String author = "test";
    String body = "test";

    public Comment(Element trElement) throws Exception{
        trElement.child(0).select("img").remove();
        author = trElement.child(0).toString();
        body = trElement.child(1).toString();
    }

    @Override
    public String getHTML() {
        return "<h1>" + author + "</h1><br />" +
                body;
    }
}
