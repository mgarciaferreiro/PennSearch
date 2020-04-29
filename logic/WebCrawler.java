package logic;

import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import data.DBManager;

/**
 * This class handles crawling through the Penn websites and storing the data in
 * the database.
 * 
 * @author Peter Chen
 */
public class WebCrawler {
    private HashSet<String> visitedSet = new HashSet<>();
    private ArrayList<String> ls = new ArrayList<>();
    private DBManager db = new DBManager();
    private long crawledNumber = 0;

    /**
     * checks if a URL is Penn-related. This ensures that we are staying
     * within the Penn network
     * @param url
     * @return true is the url is valid, false otherwise
     */
    private Boolean isValid(String url) {
        if (url.contains("http") && url.contains("penn")) return true;
        return false;
    }

    /**
     * define a discover subroutine 
     * given a source link, add its neighbors to the
     * visited set and the list if these are not discovered yet
     */
    private void discover(String sourceURL) {
        try {
            Document doc = Jsoup.connect(sourceURL).get();
            ArrayList<String> neighbors = new ArrayList<>();

            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                String url = link.attr("abs:href");
                neighbors.add(url);
                
                if (!visitedSet.contains(url) && isValid(url)) {
                    ls.add(url);
                    visitedSet.add(url);
                }
            }

            db.addWebsite(sourceURL, doc.title(), doc.body().text(), neighbors);
            
            if (crawledNumber % 50 == 0) {
                System.out.println("PennSeach has crawled " + crawledNumber + " websites");
            }
            crawledNumber++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crawl() {
        // initialize
        String startURL = "https://www.upenn.edu/";
        discover(startURL);

        // set a bound on the number of URL to prevent it from running infinitely
        int maxLinkNo = 1000;

        while (!ls.isEmpty() && crawledNumber < maxLinkNo) {
            String sourceURL = ls.remove(0);
            // System.out.println("the link " + sourceURL + " is popped");
            discover(sourceURL);
        }

        db.close();
    }
}