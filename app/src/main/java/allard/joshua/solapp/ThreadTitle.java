package allard.joshua.solapp;

import android.net.Uri;
import android.util.Log;

import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThreadTitle implements HTMLListItem{
	private String title;
	private Date modifiedDateObject;
	private DateFormat df = new SimpleDateFormat("MMMM dd yyyy, hh:mm:ss a", Locale.ENGLISH);
	private String modifiedBy;
	public String url;
	private String id;
	public String topicNum = "";
	public String pageNum = "";

	public ThreadTitle(String titleLine, String line2, String line3, String line4) throws ParseException {
		title = titleLine.replaceAll(".*lastpost=1'>", "").replaceAll("</a>.*<br />", "");
		String modifiedDate = line3.replaceAll(".*<td align='center'>", "").replace("<br />", "");
		modifiedBy = line4.replaceAll(".*'>", "").replace("</a></td> </tr>", "").replace("<font color=red>", "").replace("</font>", "");
		id = line4.trim().replace("By: <a href='viewuser.php?u=", "").replaceAll("'>.*", "");
		url = "http://www.samuraioflegend.com/" + titleLine.replaceAll(".*href='", "").replaceAll("lastpost=1'>.*", "lastpost=1");
        url = url.split("com/")[1];

        Uri u = Uri.parse(url);
        topicNum = u.getQueryParameter("viewtopic");
        //Log.d("url", url);
        //Log.d("uri - ", topicNum);

        //System.out.println(title + " - " + modifiedDate);
		
		//String target = "Thu Sep 28 20:29:30 JST 2000";
		modifiedDateObject = getDate(modifiedDate); 
	    
	}

    public ThreadTitle(Element linkElement){
        url = linkElement.attr("href");
        Uri u = Uri.parse(url);
        topicNum = u.getQueryParameter("viewtopic");
        pageNum = linkElement.text();
        //Log.d("topic", topicNum);
        //Log.d("page", pageNum);

    }

    public ThreadTitle(String url){
        this.url = url;
        //Log.d("topic", topicNum);
        //Log.d("page", pageNum);

    }

	private Date getDate(String modifiedDate) throws ParseException {
		modifiedDate = modifiedDate.replace("am", "AM").replace("pm", "PM");
		Date date = df.parse(modifiedDate);
		//System.out.println(df.format(date));
		return date;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String getHTML() {
		return "<h1>" + title + "</h1>"
                + "<span>Latest Post: " + df.format(modifiedDateObject) + "<br />"
                + "By: " + modifiedBy + "</span>";
	}
}
