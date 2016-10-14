package allard.joshua.solapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MailboxActivity extends BaseActivity {

    public static List<String> lines;
    public static List<Mail> mails;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_mailbox);
        try {
            TextView text = (TextView) findViewById(R.id.textViewMain);
            text.setText("Mails Displayed: " + mails.size());

            ItemScroll mailView = (ItemScroll) findViewById(R.id.mailScroll);
            mailView.populateItemScroll(15, this, mails, false);
            mailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MailReplyActivity.returnToActivity(activity, mails.get(position));
                }
            });
        }
        catch(Exception e){
            Log.d("Debug", "Failed: " + e.getMessage());
        }
        //setContentView(R.layout.activity_main);
    }

    public static void returnToActivity(final Activity activity){
        try {
            lines = Connector.loadPage(null, "mailbox.php?action=viewmore");
            mails = processMail(lines);
        }
        catch(Exception e){
            Log.d("Debug", e.toString());
        }
        Intent mainIntent = new Intent(activity, MailboxActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void refresh(){
        returnToActivity(this);
    }

    private static List<Mail> processMail(List<String> pageLines){
        List<Mail> mails = new ArrayList<>();
        boolean adding = false;
        Mail currentMail = null;
        for(String line: pageLines){
            if(adding){


                if(line.contains("javascript:checkAll")){
                    if(currentMail != null){
                        currentMail.processLines();
                        mails.add(currentMail);
                        //Log.d("Debug", currentMail.mailLines.get(0));
                    }
                    adding = false;
                }
                else if(line.contains("</tr><tr><td align=center>")){
                    if(currentMail != null){
                        currentMail.processLines();
                        mails.add(currentMail);
                        //Log.d("Debug", currentMail.mailLines.get(0));
                    }
                    currentMail = new Mail();
                    currentMail.addLine(line);
                }
                else{
                    if(currentMail != null) {
                        currentMail.addLine(line);
                    }
                }
            }

            if(line.contains("Subject/Message")){
                adding = true;
            }
        }

        return mails;
    }


    @Override
    public void onBackPressed() {
        MainActivity.returnToMainActivity(this);
    }
}
