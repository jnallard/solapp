package allard.joshua.solapp.page.correction;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class MailboxPageCorrector implements IPageCorrector {
    @Override
    public void correctPage(Document doc) {
        Elements messageButtonsTable = doc.select("table[width=\"80%\"]");
        for(Element element: messageButtonsTable) {
            element.select("tr").tagName("div").addClass("row");
            element.select("td").tagName("div").addClass("col-xs-12");


            Elements linksToModify = element.select("a").addClass("btn").addClass("btn-default").addClass("btn-block").addClass("top-margin");
            for(Element link: linksToModify){
                for(Node sibling: link.siblingNodes()) {
                    Log.d("sibling-html", sibling.toString());
                    if(sibling.toString().contains("[") || sibling.toString().contains("]")) {
                        sibling.remove();
                    }
                }
            }
        }
        Log.d("new-html", messageButtonsTable.html());
    }
}
