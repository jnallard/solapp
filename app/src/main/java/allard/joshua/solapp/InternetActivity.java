package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpCookie;

public class InternetActivity extends BaseActivity {
    private static String pageUrl;

    private static String solJs = "";
    private static String jqueryJs = "";
    private static boolean isFirst;
    private static String setCookie = "";
    private WebView mWebView;


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
            //SystemClock.sleep(1000);
            /*Log.d("cookie domain", c.getDomain());
            cookie = c;*/
        }

        cookieSyncManager.sync();
       // SystemClock.sleep(1000);


        //mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        isFirst = true;


        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                cookieManager.setCookie(url, setCookie);
                return super.shouldOverrideUrlLoading(view, url);
                //return true;
            }

            /*@Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                cookieManager.setCookie(url, setCookie);
                return super.shouldInterceptRequest(view, url);
            }*/

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


                //String javascript = "<P>Hello World</p>";
                //view.loadUrl("https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js");
                //view.loadUrl("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css");
                //view.loadUrl("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //CookieManager.getInstance().flush();
                super.onPageFinished(view, url);
                String javascript = "javascript: " + solJs;
                view.loadUrl(javascript);

            }

        });

        /*if(cookie != null) {
            CookieSyncManager.createInstance(mWebView.getContext());
            CookieSyncManager.getInstance().startSync();
            //CookieManager cookieManager = CookieManager.getInstance();
            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager.getInstance().removeSessionCookie();
            SystemClock.sleep(1000);
            CookieManager.getInstance().setCookie("http://" + cookie.getDomain() + "/", cookie.toString());
            CookieSyncManager.getInstance().sync();
        }*/

        /*try {
            String postMessage = "username=" + URLEncoder.encode(Connector.username, "utf-8") + "&password=" + URLEncoder.encode(Connector.password, "utf-8") + "&myselect=World+1&login=Login";
            URL siteUrl = new URL("http://www.samuraioflegend.com/authenticate.php");
            mWebView.postUrl(siteUrl.toString(), postMessage.getBytes());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }*/
        mWebView.loadUrl(pageUrl);
        //setContentView(mWebView);

        //setContentView(R.layout.activity_main);

        setTitle("Web View");
    }

    public static void returnToActivity(final Activity activity, String url){
        solJs = readFile(activity, "solload.js");
        jqueryJs = readFile(activity, "jquery.min.js");

        if(!url.startsWith("http"))
            url = "http://www.samuraioflegend.com/" + url;

        Log.d("url", url);

        InternetActivity.pageUrl = url;



        //HttpCookie cookie = null;


        Intent mainIntent = new Intent(activity, InternetActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    private static String readFile(Activity activity, String filename) {
        String file = "";
        try {
            file = "";
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

        Log.d("file", file);
        return file;
    }

    @Override
    public void refresh() {
        mWebView.reload();
    }
}
