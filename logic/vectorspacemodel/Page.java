package logic.vectorspacemodel;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This class represents one document.
 * It will keep track of the term frequencies.
 * @author swapneel
 * Modified by Marta Garcia Ferreiro
 *
 */
public class Page implements Comparable<Page> {
	
	/**
	 * A hashmap for term frequencies.
	 * Maps a term to the number of times this terms appears in this document. 
	 */
	private HashMap<String, Integer> termFrequency;
	
	private String url;
	private String title;
	private String content;
	private List<String> neighbors;
	
	/**
	 * The constructor.
	 * It will pre-process the content string.
	 * @param url the url of the page
	 * @param title the title of the page
	 * @param content the content of the page
	 * @param neighbors links to other pages in the page
	 */
	public Page(String url, String title, String content, List<String> neighbors) {
		this.url = url;
		this.title = title;
		this.content = content;
		this.neighbors = neighbors;
		termFrequency = new HashMap<String, Integer>();
		
		createTermFrequencyMap();
	}
	
	/**
	 * This method will read in the page content and do some pre-processing.
	 * The following things are done in pre-processing:
	 * Every word is converted to lower case.
	 * Every character that is not a letter or a digit is removed.
	 * We don't do any stemming.
	 * Once the pre-processing is done, we create and update the termFrequency map
	 */
	private void createTermFrequencyMap() {
		System.out.println("Reading page: " + url + " and preprocessing");

		String[] contentArray = content.split("\\P{L}+");
		
		for (String word : contentArray) {
			String filteredWord = word.toLowerCase();//word.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
			
			if (!(filteredWord.equalsIgnoreCase(""))) {
				if (termFrequency.containsKey(filteredWord)) {
					int oldCount = termFrequency.get(filteredWord);
					termFrequency.put(filteredWord, ++oldCount);
				} else {
					termFrequency.put(filteredWord, 1);
				}
			}
		}
	}
	
	/**
	 * This method will return the term frequency for a given word.
	 * If this document doesn't contain the word, it will return 0
	 * @param word The word to look for
	 * @return the term frequency for this word in this document
	 */
	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}
	
	/**
	 * This method will return a set of all the terms which occur in this document.
	 * @return a set of all terms in this document
	 */
	public Set<String> getTermList() {
		return termFrequency.keySet();
	}

	/**
	 * @return a list of all neighbor urls
	 */
	public List<String> getNeighbors() {
		return neighbors;
	}

	@Override
	/**
	 * The overriden method from the Comparable interface.
	 */
	public int compareTo(Page other) {
		return url.compareTo(other.getUrl());
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * This method is used for pretty-printing a Document object.
	 * @return the url
	 */
	public String toString() {
		return url;
	}
}