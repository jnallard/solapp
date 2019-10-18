package allard.joshua.solapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.Stack;

import allard.joshua.solapp.parser.BaseTemplateParser;
import allard.joshua.solapp.parser.PageParser;

public class InternetActivity extends BaseActivity {
    public static String TITLE = "SoL Mobile";
    private static String pageUrl;

    private static String templateHtml = "";
    private static String jqueryJs = "";
    private static String bootstrapJs = "";
    private static String setCookie = "";
    private WebView mWebView;
    private WebView statusView;
    private ProgressBar loadingBar;
    private boolean showingStatus = false;

    private String html = "";
    private boolean pageHandled = false;
    private String currentUrl = "";

    private Stack<String> history = new Stack<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_internet);

        if(pageUrl == null){
            LoginActivity.returnToActivity(this, "You need to log in");
            return;
        }

        mWebView = (WebView) findViewById(R.id.webView);
        statusView = (WebView) findViewById(R.id.status_view);
        statusView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showingStatus = true;
                showStatus();
                return true;
            }
        });
        loadingBar = (ProgressBar) findViewById(R.id.progressBar);

        final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();

        cookieManager.removeAllCookie();
        for (HttpCookie cookie : Connector.cm.getCookieStore().getCookies()) {

            setCookie = new StringBuilder(cookie.toString())
                    .append("; domain=").append(cookie.getDomain())
                    .append("; path=").append(cookie.getPath())
                    .toString();
            cookieManager.setCookie(pageUrl, setCookie);
            cookieSyncManager.sync();
        }

        cookieSyncManager.sync();


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);


        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                cookieManager.setCookie(url, setCookie);
                view.stopLoading();
                handlePageLoadingAsync(view, url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String newUrl = handlePostExceptions(url);
                if(newUrl != null && !pageHandled){
                    handlePageLoadingAsync(view, newUrl);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pageHandled = false;
                loadingBar.setVisibility(View.INVISIBLE);
            }

        });

        handlePageLoadingAsync(mWebView, pageUrl);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void handlePageLoadingAsync(final WebView view, final String url) {
        loadingBar.setVisibility(View.VISIBLE);
        view.post(new Runnable() {
            public void run() {
                handlePageLoad(view, url);
            }
        });
    }

    private void handlePageLoad(WebView view, String url) {
        Log.d("url", url);
        pageHandled = true;

        if (url.contains("http") && !url.contains("samuraioflegend.com")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            view.stopLoading();
            return;
        }

        if(url.contains("login.php")){
            LoginActivity.returnToActivity(activity, "You were automatically logged out");
            return;
        }

        currentUrl = url;
        history.push(url);
        pageUrl = currentUrl;
        url = url.replace( Connector.BaseUrl, "");

        try {
            Connector.loadPage(null, url, activity);
            NotificationEventReceiver.triggerNow(getApplicationContext());

            BaseTemplateParser parser = PageParser.GetTemplateInfo();

            String head = "";
            if(parser == null){
                LoginActivity.returnToActivity(activity, "You were automatically logged out");
                return;
            }
            Element contentElement = parser.GetContent();
            if(contentElement == null){
                contentElement = parser.GetNoLinksContent();
            }
            if(contentElement == null){

                Toast.makeText(this, "Cannot load page",
                        Toast.LENGTH_LONG).show();
                view.stopLoading();
                return;
            }

            String statusHtml = PageParser.GetTemplateInfo().GetStatus();
            statusView.loadDataWithBaseURL(Connector.BaseUrl + "/" + url, statusHtml, "text/html", "utf-8", "");

            Elements forms = contentElement.select("table > form");
            for(Element form: forms){
                Element parent = form.parent();
                form.remove();
                Element tr = parent.appendElement("tr");
                Element td = tr.appendElement("td");
                td.appendChild(form);
                for(Element input: parent.parent().select("input")){
                    input.remove();
                    form.appendChild(input);
                }

            }

            String content = contentElement.html();
            Elements links = parser.GetLinks();
            String[] newLinks = new String[links.size()];
            String[] newActions = new String[links.size()];
            int count = 0;

            int mailCount = parser.GetMailCount();
            int eventCount = parser.GetEventCount();
            int announcementCount = parser.GetAnnouncementCount();
            for (Element e : links) {
                newLinks[count] = e.text();
                newActions[count++] = e.attr("href");
            }

            if(mailCount + eventCount + announcementCount > 0){
                String newTitle = "New ";
                if(mailCount > 0){
                    newTitle += "Mails (" + mailCount + "), ";
                }
                if(eventCount > 0){
                    newTitle += "Events (" + eventCount + "), ";
                }
                if(announcementCount > 0){
                    newTitle += "Announcements (" + announcementCount + "), ";
                }
                setTitle(newTitle);
            }
            else{
                setTitle(TITLE);
            }
            NavigationDrawerFragment.updateLinks(newLinks);
            setActions(newActions);

            html = templateHtml.replace("[[SCRIPTS]]", jqueryJs + bootstrapJs).replace("[[CSS]]", getCSS()).replace("[[HEAD]]", head).replace("[[CONTENT]]", content);
            html = html.replace("[[BG_COLOR]]", parser.GetBGColor()).replace("[[TEXT_COLOR]]", parser.GetTextColor()).replace("[[LINK_COLOR]]", parser.GetLinkColor());
            html = html.replaceAll("<img", "<img class=\"img img-responsive\" ");
            html = html.replaceAll("<input", "<input class=\"btn btn-block\" ");
            html = html.replaceAll("width=\"([0-9]{1,4})\"", "");
            Log.d("html", html);

            view.stopLoading();
            view.loadDataWithBaseURL(Connector.BaseUrl + "/" + url, html, "text/html", "utf-8", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String handlePostExceptions(String url) {
        if (url.contains("/forums.php?reply")) {
            return url.replace("reply", "viewtopic") + "&lastpost=1";
        }
        if (url.contains("/mailbox.php?action=send")) {
            return url.replace("?action=send", "");
        }
        if (url.contains("/viewuser.php?u=")) {
            return url;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() && !history.empty()) {
            history.pop();
            if(!history.empty()){
                loadingBar.setVisibility(View.VISIBLE);
                pageUrl = history.peek();
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.goBack();
                    }
                });
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void loadPage(BaseActivity activity, String url) {
        pageUrl = url;
        handlePageLoadingAsync(mWebView, url);
    }

    public static void returnToActivity(final Activity activity, String url) {
        jqueryJs = getScript(activity, "jquery.min.js");
        bootstrapJs = getScript(activity, "bootstrap.min.js");
        templateHtml = getFile(activity, "template.html");

        if (!url.startsWith("http"))
            url = Connector.BaseUrl + "/" + url;

        Log.d("url", url);

        InternetActivity.pageUrl = url;

        Intent mainIntent = new Intent(activity, InternetActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    private static String getScript(Context activity, String filename) {
        String file = getFile(activity, filename);

        Log.d("file", file);
        return "<script>" + file + "</script>";
    }

    @NonNull
    private static String getFile(Context activity, String filename) {
        String file = "";
        try {
            InputStreamReader is = new InputStreamReader(activity.getAssets()
                    .open(filename));

            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                file += line;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    private String getCSS() {
        return "<style>" + getFile(this, "bootstrap.min.css") + "</style>";
    }

    @Override
    public void refresh() {
        handlePageLoadingAsync(mWebView, pageUrl);
    }

    @Override
    public void showStatus() {
        showingStatus = !showingStatus;
        if(showingStatus){
            statusView.setVisibility(View.VISIBLE);
            mWebView.setEnabled(false);
        }
        else{
            statusView.setVisibility(View.GONE);
            mWebView.setEnabled(true);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Internet Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
