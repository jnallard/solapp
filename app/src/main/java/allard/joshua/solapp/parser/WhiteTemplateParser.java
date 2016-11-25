package allard.joshua.solapp.parser;

/**
 * Created by Joshua on 10/14/2016.
 */

public class WhiteTemplateParser extends BaseTemplateParser {

    @Override
    public String GetBGColor() {
        return "background-color: white;";
    }

    @Override
    public String GetLinkColor() {
        return "black";
    }

    @Override
    public String GetTextColor() {
        return "black";
    }
}
