package allard.joshua.solapp.parser;

/**
 * Created by Joshua on 10/14/2016.
 */

public class BrownTemplateParser extends BaseTemplateParser {

    @Override
    public String GetBGColor() {
        return "    background-image: url(images5/body_hrepeat.jpg);\n" +
                "    background-attachment: scroll;\n" +
                "    background-repeat: repeat-x;\n" +
                "    background-color: #000000;";
    }

    @Override
    public String GetLinkColor() {
        return "#be8b5e";
    }

    @Override
    public String GetTextColor() {
        return "#c4ad9b";
    }
}
