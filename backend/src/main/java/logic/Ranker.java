package logic;

import java.util.*;
import java.util.Map.Entry;

import logic.vectorspacemodel.*;

/**
 * This class assigns a rank to each search result based on page authoritativeness 
 * and relevance of document to query.
 * 
 * @author Marta Garcia Ferreiro
 */
public class Ranker {

    private Map<String, List<String>> websiteGraph;
    private ArrayList<Page> documents;

    Corpus corpus;
    VectorSpaceModel vectorSpace;

    // maps page url to page rank
    private Map<String, Double> prMap;

    public Ranker(ArrayList<Page> documents) {

        this.documents = documents;
        this.websiteGraph = new HashMap<>();

        for (Page page : documents) {
            websiteGraph.put(page.getUrl(), page.getNeighbors());
        }

        // create vector space model from documents
        this.corpus = new Corpus(documents);
        this.vectorSpace = new VectorSpaceModel(corpus);

        // calculate page rank values
        Map<String, Double> prValueMap = new HashMap<>();
        double n = documents.size();
        for (Page page : documents) {
            prValueMap.put(page.getUrl(), 1 / n);
        }
        this.prMap = pageRank(prValueMap);

    }

    public List<Page> getSortedResults(String query) {
        LinkedList<Page> sortedResults = new LinkedList<Page>();
        Map<Page, Double> pageToScoreMap = new HashMap<>();
        Page queryPage = new Page("", "", query, new ArrayList<>());

        vectorSpace.createQueryTfIdfVector(queryPage);

        for (Page doc : documents) {
            
            /* What if the document has no incoming edges so it has a page rank value 
             0? We might want to consider a weighted sum of the PR and cosine similarity
             in that case, TBD.
            */
            String url = doc.getUrl();

            double prValue = 0;
            if (prMap.containsKey(url)) {
                prValue = prMap.get(url);
            }

            double cosineSimilarity = vectorSpace.cosineSimilarity(queryPage, doc);
            double score = prValue * cosineSimilarity;
            
            if (score > 0) {
                pageToScoreMap.put(doc, score);
            }
        }
        List<Entry<Page, Double>> list = new ArrayList<>(pageToScoreMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        // By adding elements to the front of the line, they end up in descending order of score
        for (Entry<Page, Double> entry : list) {
            System.out.println("page: " + entry.getKey() + " score: " + entry.getValue());
            sortedResults.addFirst(entry.getKey());
        }

        return sortedResults;
    }

    /* 
    Returns map of Page Rank values for the website graph. The graph is represented as an
    Adjacency List that maps each node to its list of neighbors.
    Limitations: sinks, links in different parts of the page (header > paragraph), etc.
    */
    public Map<String, Double> pageRank(Map<String, Double> prValueMap) {
        
        Map<String, Double> newPrValues = new HashMap<>();

        for (String node : prValueMap.keySet()) {

            double value = prValueMap.get(node);
            List<String> neighbors = websiteGraph.get(node);

            if (neighbors != null) {
                double numNeighbors = neighbors.size();
                double valuePerNeighbor = value / numNeighbors;
                for (String neighbor : neighbors) {
                    if (newPrValues.get(neighbor) == null) {
                        newPrValues.put(neighbor, valuePerNeighbor);
                    } else {
                        double tempValue = newPrValues.get(neighbor);
                        newPrValues.put(neighbor, tempValue + valuePerNeighbor);
                    }
                }
            }
        }
        
        boolean valuesConverge = true;
        for (String node : newPrValues.keySet()) {
            double newValue = newPrValues.get(node);
            if (prValueMap.containsKey(node)) {
                double oldValue = prValueMap.get(node);
                if (Math.abs(oldValue - newValue) > 0.000001) {
                    valuesConverge = false;
                }
            }
        }

        if (valuesConverge) {
            return newPrValues;
        } else {
            return pageRank(newPrValues);
        }
    }

}