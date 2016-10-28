package allard.joshua.solapp.parser;

import java.util.List;

/**
 * Created by Joshua on 10/14/2016.
 */

public class PageParser {
    BaseTemplateParser templateParser = new BlueTemplateParser();
    public boolean loggedOut = false;

    private PageParser(){

    }

    private static PageParser instance = new PageParser();

    public static PageParser GetInstance(){
        return instance;
    }

    public PageParser Parse(List<String> lines){
        templateParser.Parse(lines);
        return this;
    }

    public static BaseTemplateParser GetTemplateInfo(){
        return GetInstance().templateParser;
    }
}
