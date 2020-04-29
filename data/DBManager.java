package data;

import java.util.ArrayList;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 * This class handles reads and writes to the Mongo DB.
 */
public class DBManager {
    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    private MongoDatabase database = mongoClient.getDatabase("pennsearch");
    private MongoCollection<Document> collection = database.getCollection("websites");

    public void addWebsite(String url, String title, String content, ArrayList<String> neighbors) {
        Document doc = new Document("url", url).append("title", title).append("content", content).append("neighbors", neighbors);
        collection.insertOne(doc);
    }

    public void close() {
        mongoClient.close();
    }
}