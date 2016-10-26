package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.List;

public class InternetActivity extends BaseActivity {
    private static String pageUrl;

    private static String solJs = "";
    private static String templateHtml = "";
    private static String jqueryJs = "";
    private static String bootstrapJs = "";
    private static String bootstrapCss = "";
    private static boolean isFirst;
    private static String setCookie = "";
    private WebView mWebView;

    private String html = "";
    private boolean loadingMyPage = false;
    private String currentUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_internet);
         mWebView = (WebView) findViewById(R.id.webView);

        final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();

        cookieManager.removeAllCookie();
        for(HttpCookie cookie: Connector.cm.getCookieStore().getCookies()){

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

        isFirst = true;


        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                cookieManager.setCookie(url, setCookie);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("url", url);
                if(loadingMyPage) return;
                currentUrl = url;
                pageUrl = currentUrl;
                url = url.replace("http://www.samuraioflegend.com", "");

                url = handlePostExceptions(url);

                try{
                    loadingMyPage = true;
                    List<String> lines = Connector.loadPage(null, url);
                    StringBuilder builder = new StringBuilder();
                    for(String s: lines){
                        builder.append(s);
                    }
                    html = builder.toString();
                    Document doc = Jsoup.parse(html);

                    String head = doc.select("head").html();
                    head = "";
                    String content = doc.select("td[width=\"83%\"]").html();
                    Elements links = doc.select("td[width=\"260\"]").select("a");
                    String[] newLinks = new String[links.size()];
                    String[] newActions = new String[links.size()];
                    int count = 0;
                    for(Element e : links){
                        newLinks[count] = e.text();
                        newActions[count++] = e.attr("href");
                    }
                    NavigationDrawerFragment.updateLinks(newLinks);
                    actions = newActions;

                    html = templateHtml.replace("[[SCRIPTS]]", jqueryJs + bootstrapJs).replace("[[CSS]]", getCSS()).replace("[[HEAD]]", head).replace("[[CONTENT]]", content);
                    html = html.replaceAll("<img", "<img class=\"img img-responsive\" ");
                    html = html.replaceAll("<input", "<input class=\"btn btn-block\" ");
                    html = html.replaceAll("width=\"([0-9]{1,4})\"", "");
                    Log.d("html", html);

                    view.stopLoading();
                    view.loadDataWithBaseURL("http://www.samuraioflegend.com/", html, "text/html", "utf-8", "");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingMyPage = false;
            }

        });

        mWebView.loadUrl(pageUrl);

        setTitle("Web View");
    }

    private String handlePostExceptions(String url){
        if(url.contains("/forums.php?reply")){
            return url.replace("reply", "viewtopic") + "&lastpost=1";
        }
        return url;
    }

    @Override
    public void onBackPressed() {
        mWebView.goBack();
    }

    public static void returnToActivity(final Activity activity, String url){
        solJs = getScript(activity, "solload.js");
        jqueryJs = getScript(activity, "jquery.min.js");
        bootstrapJs = getScript(activity, "bootstrap.min.js");
        bootstrapCss = getScript(activity, "bootstrap.min.css");
        templateHtml = getFile(activity, "template.html");

        if(!url.startsWith("http"))
            url = "http://www.samuraioflegend.com/" + url;

        Log.d("url", url);

        InternetActivity.pageUrl = url;

        Intent mainIntent = new Intent(activity, InternetActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    private static String getScript(Activity activity, String filename) {
        String file = getFile(activity, filename);

        Log.d("file", file);
        return "<script>" + file + "</script>";
    }

    @NonNull
    private static String getFile(Activity activity, String filename) {
        String file = "";
        try {
            InputStreamReader is = new InputStreamReader(activity.getAssets()
                    .open(filename));

            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                file += line;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return file;
    }

    private String getCSS() {
        return "<style>" + getFile(this, "bootstrap.min.css") + "</style>";
    }

    @Override
    public void refresh() {
        mWebView.loadUrl(currentUrl);
    }
}
