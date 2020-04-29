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

    private DBManager db = new DBManager();
    private Ranker ranker;
    private ArrayList<Page> documents;

    public Searcher() {
        this.documents = db.getPagesFromDb();
        this.ranker = new Ranker(documents);
    }

    public void search(String query) {
        ranker.getSortedResults(query);
    }

}