package logic.vectorspacemodel;

import java.util.HashMap;
import java.util.Set;

/**
 * This class implements the Vector-Space model.
 * It takes a corpus and creates the tf-idf vectors for each document.
 * @author swapneel
 * Modified by Marta Garcia Ferreiro
 */
public class VectorSpaceModel {
	
	/**
	 * The corpus of documents.
	 */
	private Corpus corpus;
	
	/**
	 * The tf-idf weight vectors.
	 * The hashmap maps a document to another hashmap.
	 * The second hashmap maps a term to its tf-idf weight for this document.
	 */
	private HashMap<Page, HashMap<String, Double>> tfIdfWeights;

	/**
	 * The query tf-idf weight vector.
	 * It's separated from the corpus tf-idf weight vectors so we don't have to recompute all 
	 * of them for each new query.
	 */
	private HashMap<String, Double> queryTfIdfWeights;
	
	/**
	 * The constructor.
	 * It will take a corpus of documents.
	 * Using the corpus, it will generate tf-idf vectors for each document.
	 * @param corpus the corpus of documents
	 */
	public VectorSpaceModel(Corpus corpus) {
		this.corpus = corpus;
		tfIdfWeights = new HashMap<Page, HashMap<String, Double>>();
		createTfIdfWeights();
	}

	/**
	 * This creates the tf-idf vectors.
	 */
	private void createTfIdfWeights() {
		System.out.println("Creating the tf-idf weight vectors");
		Set<String> terms = corpus.getInvertedIndex().keySet();
		
		for (Page document : corpus.getDocuments()) {
			HashMap<String, Double> weights = new HashMap<String, Double>();
			
			for (String term : terms) {
				double tf = document.getTermFrequency(term);
				double idf = corpus.getInverseDocumentFrequency(term);
				
				double weight = tf * idf;
				
				if (weight > 0) {
					weights.put(term, weight);
				}
			}
			tfIdfWeights.put(document, weights);
		}
	}
	
	/**
	 * This method will return the magnitude of a vector.
	 * @param document the document whose magnitude is calculated.
	 * @return the magnitude
	 */
	private double getMagnitude(Page document, boolean isQuery) {
		double magnitude = 0;
		HashMap<String, Double> weights = tfIdfWeights.get(document);
		if (isQuery) {
			weights = queryTfIdfWeights;
		}
		
		for (double weight : weights.values()) {
			magnitude += weight * weight;
		}
		
		return Math.sqrt(magnitude);
	}
	
	/**
	 * This will take a query document and another document and return the dot product.
	 * @param query Query document
	 * @param doc Document 2
	 * @return the dot product of the documents
	 */
	private double getDotProduct(Page query, Page doc) {
		double product = 0;
		HashMap<String, Double> weights2 = tfIdfWeights.get(doc);
		
		for (String term : queryTfIdfWeights.keySet()) {
			double weight1 = queryTfIdfWeights.get(term);
			double weight2 = 0;
			if (weights2.containsKey(term)) {
				weight2 = weights2.get(term);
			}
			product += weight1 * weight2;
		}
		
		return product;
	}

	/**
	 * This will compute the query tf-idf vector.
	 * @param query Query document
	 */
	public void createQueryTfIdfVector(Page query) {
		HashMap<String, Double> weights = new HashMap<String, Double>();
			
		for (String term : query.getTermList()) {
			double tf = query.getTermFrequency(term);
			double idf = corpus.getInverseDocumentFrequency(term); // 0 if the corpus doesn't contain the term
			
			double weight = tf * idf;
			
			if (weight > 0) {
				weights.put(term, weight);
			}
		}

		this.queryTfIdfWeights = weights;
	}
	
	/**
	 * This will return the cosine similarity of two documents.
	 * This will range from 0 (not similar) to 1 (very similar).
	 * @param query Query document
	 * @param doc Document 2
	 * @return the cosine similarity
	 */
	public double cosineSimilarity(Page query, Page doc) {
		return getDotProduct(query, doc) / (getMagnitude(query, true) * getMagnitude(doc, false));
	}
}