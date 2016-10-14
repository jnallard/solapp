package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailReplyActivity extends BaseActivity {

    public static List<String> lines;
    public static ArrayList<String> previousMessages;

    private static Mail mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_mail_reply);
        TextView title = (TextView) findViewById(R.id.title_reply);
        title.setText(Html.fromHtml("Mailing " + mail.user));

        ListView history = (ListView) findViewById(R.id.listView);
        ArrayList<TextView> items = new ArrayList<>();
        if(previousMessages != null) {
            for (String s : previousMessages) {
                TextView tv = new TextView(this);
                tv.setText(s);
                items.add(tv);
            }
        }
        history.setAdapter(new ItemScroll.ItemListAdapter(this, android.R.layout.simple_spinner_item, items, true));
    }




    @Override
    public void onBackPressed() {
        MailboxActivity.returnToActivity(this);
    }

    public static void returnToActivity(final Activity activity, Mail mail){
        MailReplyActivity.mail = mail;
        try {
            lines = Connector.loadPage(null, "mailbox.php?action=compose&ID=NEW_ID&more=1".replace("NEW_ID", mail.id));
            processLines();
        }
        catch(Exception e){
            Log.d("Debug", e.toString());
        }
        Intent mainIntent = new Intent(activity, MailReplyActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }



    @Override
    public void refresh(){
        returnToActivity(this, mail);
    }

    private static void processLines(){
        boolean adding = false;
        String fullLine = "";
        for(String line: lines){
            if(line.contains("Show More With This Samurai")){
                fullLine += line;
                adding = true;
            }
            else if(adding){
                fullLine += line;
            }

            if((line.contains("<iframe") || line.contains("</div>") || line.contains("<script>")) & adding){
                adding = false;

                previousMessages = processPreviousMessages(fullLine);
            }
        }
    }

    private static ArrayList<String> processPreviousMessages(String line){
        line = line.replace("</td> <td><b>", "<br /><b>");
        ArrayList list =  new ArrayList<String>(Arrays.asList(line.split("</tr>")));
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(list.size() - 1);
        list.remove(list.size() - 1);
        return list;
    }

    public void sendReply(View view){
        try {
            EditText text = (EditText) findViewById(R.id.editText3);
            String user = URLEncoder.encode(mail.user, "utf-8");
            String message = URLEncoder.encode(text.getText().toString(), "utf-8");
            String subject = URLEncoder.encode("Sent by SoL App", "utf-8");

            String post = "user1=&user2=" + user + "&subject=" + subject + "&message=" + message;
            //Log.d("Debug", post);
            Connector.loadPage(post, "mailbox.php?action=send");
            returnToActivity(this, mail);
        }
        catch(Exception e){

        }
    }


    public void userClick(View view){
        try {
            UserActivity.returnToActivity(this, mail.id);
        }
        catch(Exception e){
            Log.d("Debug", "User Activity Failed: " + e.getMessage());
        }
    }
}
