package allard.joshua.solapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joshua on 11/4/2015.
 */
public class Mail implements HTMLListItem {
    public List<String> mailLines = new ArrayList<>();
    String user = "";
    String id = "";
    String subject = "";
    String date = "";
    String message = "";

    public void processLines(){

        processFirstLine();
        getDate();
        getMessage();
    }

    public void addLine(String line){
        mailLines.add(line);
    }

    private void processFirstLine(){
        String firstLine = mailLines.get(0);
        user = firstLine.substring(firstLine.indexOf("<b>") + 3, firstLine.indexOf("</b>"));
        id = firstLine.substring(firstLine.indexOf("</a> [") + 6, firstLine.indexOf("]</td>"));
    }

    private void getDate(){
        String dateLine = mailLines.get(3);
        date = dateLine.substring(dateLine.indexOf("<td>") + 4, dateLine.indexOf("<br />"));
    }

    private void getMessage(){
        for(int i = 9; i < mailLines.size(); i++){
            message += mailLines.get(i);
        }
    }

    public String getHTML(){
        return "<h1>" + this.user + " [" + this.id + "]" + "</h1>" +
                "<b>" + this.date + "</b>" + "<br /><br />" +
                this.message;
    }
}
