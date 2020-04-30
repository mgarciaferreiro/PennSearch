package data;

import java.util.*;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;

import org.bson.Document;

import logic.vectorspacemodel.Page;

/**
 * This class handles reads and writes to the Mongo DB.
 */
public class DBManager {
    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    private MongoDatabase database = mongoClient.getDatabase("pennsearch");
    private MongoCollection<Document> collection = database.getCollection("websites");

    public void addWebsite(String url, String title, String content, ArrayList<String> neighbors) {
        Document doc = new Document("url", url).append("title", title).append("content", content).append("neighbors",
                neighbors);
        collection.insertOne(doc);
    }

    public void close() {
        mongoClient.close();
    }

    public ArrayList<Page> getPagesFromDb() {
        ArrayList<Page> pages = new ArrayList<>();

        FindIterable<Document> findIterable = collection.find(new Document());

        Block<Document> savePageBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {

                String url = document.getString("url");
                String title = document.getString("title");
                String content = document.getString("content");

                List<String> neighbors = (List<String>) document.get("neighbors");

                pages.add(new Page(url, title, content, neighbors));
            }
        };

        findIterable.forEach(savePageBlock);

        close();
        
        return pages;
    }
}