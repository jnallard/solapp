package allard.joshua.solapp.page.correction;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ViewUserPageCorrector implements IPageCorrector {
    @Override
    public String correctPage(Document doc) {
        Element commentForm = doc.select("form[action*=\"viewuser.php\"]").first();
        if(commentForm != null) {
            commentForm.attr("action", commentForm.attr("action") + "&comment-sol-app=true");
            Log.d("new-html", commentForm.outerHtml());
        }
        String html = doc.html();
        return html;
    }
}
