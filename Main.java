import logic.Searcher;
import logic.WebCrawler;

/**
 * Main function
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("running...");
        WebCrawler wc = new WebCrawler();
        wc.crawl();

        Searcher searcher = new Searcher();
        searcher.search("athletics baseball");
    }
}