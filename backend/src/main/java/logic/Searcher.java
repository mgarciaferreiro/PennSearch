package logic;

import java.util.*;

import data.DBManager;
import logic.vectorspacemodel.*;

/**
 * Gets all the pages from database and performs search.
 * 
 * @author Marta Garcia Ferreiro
 */
public class Searcher {

    private DBManager db;
    private Ranker ranker;
    private ArrayList<Page> documents;

    public Searcher() {
        this.db = new DBManager();
        this.documents = db.getPagesFromDb();
        this.ranker = new Ranker(documents);
    }

    public List<PageSummary> search(String query) {
        List<Page> pages = ranker.getSortedResults(query);
        ArrayList<PageSummary> result = new ArrayList<>();
        for (int i = 0; i < Math.min(pages.size() - 1, 30); i++) {
            Page page = pages.get(i);
            result.add(new PageSummary(page.getUrl(), page.getTitle(), page.getContent(query)));
        }
        
        return result;
        // for (Page page : pages) {
        //     System.out.println(page.getTitle());
        //     System.out.println(page.getUrl());
        // }
    }

}