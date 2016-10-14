package allard.joshua.solapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Implementation of LinkMovementMethod to allow the loading of
 * a link clicked inside text inside an Android application
 * without exiting to an external browser.
 *
 * @author Isaac Whitfield
 * @version 25/08/2013
 */
public class CustomLinkMovementMethod extends android.text.method.LinkMovementMethod {
    // The context we pass to the method
    private static Activity movementContext;
    // A new LinkMovementMethod
    private static CustomLinkMovementMethod linkMovementMethod  = new CustomLinkMovementMethod();

    public static MovementMethod getInstance(Activity c){
        // Set the context
        movementContext = c;
        // Return this movement method
        return linkMovementMethod;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event){
        //if(true) return false;
        // Get the event action
        int action = event.getAction();

        // If action has finished
        if(action == MotionEvent.ACTION_UP) {
            // Locate the area that was pressed
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();

            // Locate the URL text
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            // Find the URL that was pressed
            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            // If we've found a URL
            if (link.length != 0) {
                // Find the URL
                String url = link[0].getURL();
                // If it's a valid URL
                if(url.contains("forums.php?viewtopic")){
                    Uri u = Uri.parse(url);
                    String topic = u.getQueryParameter("viewtopic");
                    String post = u.getQueryParameter("st");
                    String newUrl = "forums.php?viewtopic=" + topic;
                    if(post != null){
                        newUrl += "&st=" + post;
                    }
                    Log.d("path", newUrl);
                    //UserActivity.returnToActivity(movementContext, user);
                    ThreadActivity.returnToActivity(movementContext, new ThreadTitle(newUrl));

                }
                else if(url.contains("viewuser.php")){
                    Uri u = Uri.parse(url);
                    String user = u.getQueryParameter("u");
                    UserActivity.returnToActivity(movementContext, user);

                }
                else if (!url.contains("samuraioflegend") & (url.contains("https") | url.contains("tel") | url.contains("mailto") | url.contains("http") | url.contains("https") | url.contains("www"))){
                    // Open it in an instance of InlineBrowser
                    //movementContext.startActivity(new Intent(movementContext, InternetActivity.class).putExtra("url", url));
                    //InternetActivity.returnToActivity(movementContext, url);
                    return super.onTouchEvent(widget, buffer, event);
                }
                else {
                    Toast.makeText(movementContext, url, Toast.LENGTH_SHORT).show();
                    InternetActivity.returnToActivity(movementContext, url);
                }
                // If we're here, something's wrong
                return true;
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}
