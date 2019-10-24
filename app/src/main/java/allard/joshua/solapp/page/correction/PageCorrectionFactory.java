package allard.joshua.solapp.page.correction;

public class PageCorrectionFactory {
    private static final PageCorrectionFactory ourInstance = new PageCorrectionFactory();

    public static PageCorrectionFactory getInstance() {
        return ourInstance;
    }

    private PageCorrectionFactory() {
    }

    public IPageCorrector getPageCorrector(String url) {
        if(url.contains("mailbox.php")) {
            return new MailboxPageCorrector();
        }
        return null;
    }
}
