package com.github.jmodel.search.api;

public interface SearchEngine {

	public String index(String indexName, String type, String json);

	public String get(String indexName, String type, String id);

	public void delete(String indexName, String type, String id);

	public void update(String indexName, String type, String id, String json);
	
	public String search(String indexName, String json);

	public void close();

}
