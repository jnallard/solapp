package allard.joshua.solapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import allard.joshua.solapp.parser.PageParser;


public class Connector {
    private static final int TIMEOUT_MILLIS = 600000;

    public static String username;
    public static String password;
    public static String BaseUrl = "http://www.samuraioflegend.com";
    public static String WorldSelect = "World+1";

    public static boolean loggedIn = false;

    public static java.net.CookieManager cm;

    public static void login(Context main, String username, String password) {
        Connector.username = username;
        Connector.password = password;
        runProgram runner = new runProgram(main);
        runner.start();
    }


    private static class runProgram extends Thread {
        private Context activity;

        public runProgram(Context activity){
            this.activity = activity;
        }

        @Override
        public void run() {
            try {

                Log.d("Debug", "0");
                //Connector.activity.text.setText("Test");
                Log.d("Debug", "0.1");
                cm = new java.net.CookieManager();
                java.net.CookieHandler.setDefault(cm);

                String postMessage = "username=" + URLEncoder.encode(username, "utf-8") + "&password=" + URLEncoder.encode(password, "utf-8") + "&myselect=" + WorldSelect + "&login=Login";
                URL siteUrl = new URL(BaseUrl + "/authenticate.php");

                List<String> lines = connectToUrl(activity, postMessage, siteUrl);
                Log.d("Debug", "3: " + lines.size());
                if (lines.size() > 5) {
                    Connector.loggedIn = true;
                    PageParser.GetInstance().Parse(lines);
                    if(activity instanceof Activity) {
                        InternetActivity.returnToActivity((Activity)activity, "/explore.php");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> loadPage(String postMessage, String url, Context activity) throws Exception {
        URL siteUrl = new URL(BaseUrl + "/" + url);
        loadPageRunner runner = new loadPageRunner(siteUrl, postMessage, activity);
        synchronized (runner.o) {

            runner.start();
            runner.o.wait();
        }
        return runner.lines;
    }

    private static class loadPageRunner extends Thread {

        URL siteUrl;
        String postMessage;
        Context activity;
        List<String> lines;
        public Object o;

        public loadPageRunner(URL siteUrl, String postMessage, Context activity) {
            this.siteUrl = siteUrl;
            this.postMessage = postMessage;
            this.activity = activity;
            o = new Object();
        }

        @Override
        public void run() {


            synchronized (o) {

                try {

                    lines = connectToUrl(activity, postMessage, siteUrl);
                    //Connector.activity.text.setText("Success ");

                } catch (Exception e) {
                    //Connector.activity.text.setText("Error 1: " + e.getMessage());
                    Log.d("Debug", "Failed");
                }
                o.notifyAll();
            }
        }
    }

    private static List<String> connectToUrl(Context activity, String postMessage, URL url) throws IOException {

        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            InputStream is = null;
            try {
                //URL url = new URL(siteUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT_MILLIS);
                connection.setReadTimeout(TIMEOUT_MILLIS);
                connection.setDoOutput(true);
                connection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
                connection.setRequestProperty("Referer", BaseUrl + "/login.php");
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
                connection.setRequestProperty("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                // connection.connect();

                if (postMessage != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(postMessage);
                    writer.flush();
                    writer.close();
                }
                int response = connection.getResponseCode();
                Log.d("Debug", "The response is: " + response);
                is = connection.getInputStream();

                Log.d("Debug", "1");
                // Convert the InputStream into a string

                if ("gzip".equals(connection.getContentEncoding())) {
                    is = new GZIPInputStream(is);
                }
                List<String> lines = new ArrayList<String>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    // System.out.println(line);
                    lines.add(line);
                }

                PageParser.GetInstance().Parse(lines);
                return lines;
                //return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                e.printStackTrace();

                Log.d("Debug", "Error: " + e.getMessage());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else {
            // display error
        }

        List<String> lines = new ArrayList<String>();
        return lines;
    }


}
