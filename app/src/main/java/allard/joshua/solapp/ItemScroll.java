package allard.joshua.solapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Joshua on 10/13/2015.
 */
public class ItemScroll extends ListView {


    private static Activity context;

    public ItemScroll(Context context) {
        super(context);
    }

    public ItemScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public void populateItemScroll(int displayCount, final Activity context, final List<? extends HTMLListItem> mails, boolean allowLinkClick) {
        this.context = context;

        final ArrayList<TextView> items = new ArrayList<>();
        //boolean showAll = displayCount == -1;


        if (mails.size() > 0) {
            for (int i = 0; i < mails.size(); i++) {
                TextView textView = new TextView(context);
                textView.setText(mails.get(i).getHTML());



                items.add(textView);
            }
        }
        else{
            TextView textView = new TextView(context);
            textView.setText("<br /><br /><br /><br /><br /><br />" +
                    "<h1'>There are no items to show.<h1><br /><br /><br /><br /><br /><br />");
            items.add(textView);
        }

        final ArrayAdapter<TextView> adapter = new ItemListAdapter(context,
                android.R.layout.simple_spinner_item, items, allowLinkClick);

        // Here, you set the data in your ListView
        this.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public static class ItemListAdapter extends ArrayAdapter<TextView> {

        public ArrayList<TextView> items;
        private boolean allowLinkClick;
        public ItemListAdapter(Activity context, int layoutId, ArrayList<TextView> items, boolean allowLinkClick) {
            super(context, layoutId, items);
            this.items = items;
            this.allowLinkClick = allowLinkClick;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            View mView = v;

            //if(mView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(R.layout.row_text, null);
            // }

            TextView text = (TextView) mView.findViewById(R.id.textView);
            //((TextView)text).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            if(allowLinkClick) text.setMovementMethod(CustomLinkMovementMethod.getInstance(context));


            if (items.get(position) != null) {
                text.setTextColor(Color.BLACK);
                text.setTextSize(10);
                text.setText(Html.fromHtml(items.get(position).getText() + ""));
                text.setTextIsSelectable(false);

            }

            return mView;
        }
    }
}
