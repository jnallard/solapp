package allard.joshua.solapp;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Joshua on 3/14/2016.
 */
public class Post implements HTMLListItem {
    String postNum = "";
    String author = "";
    String body = "";

    public Post(Element firstElement, Element secondElement) throws Exception{

        Elements elements = secondElement.select("img");
        // rename all 'font'-tags to 'span'-tags, will also keep attributs etc.
        for(Element e: elements){
            e.tagName("a");
            e.attr("href", e.attr("src"));
            e.text(e.attr("src"));
        }
        //elements.tagName("a");

        postNum = firstElement.child(0).text();
        author = secondElement.child(0).child(0).toString();
        body = secondElement.child(1).toString();

    }

    @Override
    public String getHTML() {
        return "<h1>" + author + "</h1>" +
                body;
    }
}
