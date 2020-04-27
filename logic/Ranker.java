package logic;

import java.util.*;
import java.util.Map.Entry;
import logic.vectorspacemodel.*;

/**
 * This class assigns a rank to each search result based on page authoritativeness 
 * and relevance of document to query.
 */
public class Ranker {

    private Map<String, List<String>> websiteGraph;
    private Set<String> websiteNames;
    private List<Document> documents;
    private Document query;
    private Corpus corpus;
    private VectorSpaceModel vectorSpace;
    private Map<String, Double> prMap;

    public Ranker(Map<String, List<String>> websiteGraph, ArrayList<Document> documents, Document query) {
        this.websiteGraph = websiteGraph;

        // documents must include query
        this.query = query;
		this.corpus = new Corpus(documents);
        this.vectorSpace = new VectorSpaceModel(corpus);

        // calculate page rank values
        Map<String, Double> prValueMap = new HashMap<>();
        this.websiteNames = websiteGraph.keySet();
        double n = websiteNames.size();
        for (String name : websiteGraph.keySet()) {
            prValueMap.put(name, 1 / n);
        }
        this.prMap = pageRank(prValueMap);

    }

    public List<String> getSortedResults() {
        LinkedList<String> sortedResults = new LinkedList<String>();
        Map<String, Double> nameToScoreMap = new HashMap<>();

        for (Document doc : documents) {

            String name = doc.getName();
            
            /* What if the document has no incoming edges so it has a page rank value 
             0? We might want to consider a weighted sum of the PR and cosine similarity
             in that case, TBD.
            */

            double prValue = 0;
            if (prMap.containsKey(name)) {
                prValue = prMap.get(name);
            }
            double cosineSimilarity = vectorSpace.cosineSimilarity(query, doc);

            double score = prValue * cosineSimilarity;

            nameToScoreMap.put(name, score);
        }
        List<Entry<String, Double>> list = new ArrayList<>(nameToScoreMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        // By adding elements to the front of the line, they end up in descending order of score
        for (Entry<String, Double> entry : list) {
            sortedResults.addFirst(entry.getKey());
        }

        return sortedResults;
    }

    /* 
    Returns map of Page Rank values for the website graph. The graph is represented as an
    Adjacency List that maps each node ot its list of neighbors.
    Limitations: sinks, links in different parts of the page (header > paragraph), etc.
    */
    public Map<String, Double> pageRank(Map<String, Double> prValueMap) {
        
        Map<String, Double> newPrValues = new HashMap<>();

        for (String node : websiteGraph.keySet()) {
            double value = prValueMap.get(node);
            List<String> neighbors = websiteGraph.get(node);
            double numNeighbors = neighbors.size();
            double valuePerNeighbor = value / numNeighbors;
            for (String neighbor : neighbors) {
                if (newPrValues.get(neighbor) == null) {
                    newPrValues.put(neighbor, valuePerNeighbor);
                } else {
                    newPrValues.put(neighbor, newPrValues.get(neighbor) + value);
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