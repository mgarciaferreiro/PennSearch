package com.example.restservice;

import logic.*;

import java.util.List;

public class Query {
	// private String content;
	private List<PageSummary> queryAnswers;

	public Query(String query) {
		Searcher searcher = new Searcher();
		queryAnswers = searcher.search(query);
		// this.content = query;
	}

	public List<PageSummary> getQuery() {
		return queryAnswers;
	}

	// public String getContent() {
	// 	return content;
	// }
}
