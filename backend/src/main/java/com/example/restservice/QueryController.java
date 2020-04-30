package com.example.restservice;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/query")
	public Query query(@RequestParam(value = "query", defaultValue = "World") String query) {
		return new Query(query);
	}
}
