package com.github.jmodel.search.es5;

import java.net.InetAddress;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.github.jmodel.search.api.SearchEngine;

public class ES5SearchEngine implements SearchEngine {

	private static ES5SearchEngine instance;
	private TransportClient client;

	private ES5SearchEngine() {
		client = this.getClient();
	}

	public final static synchronized ES5SearchEngine getInstance() {
		if (instance == null) {
			instance = new ES5SearchEngine();
		}
		return instance;
	}

	@SuppressWarnings("resource")
	private TransportClient getClient() {
		if (client == null) {
			try {
				Settings settings = Settings.builder().put("client.transport.sniff", true).build();
				client = new PreBuiltTransportClient(settings)
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return client;

	}

	public void close() {
		try {
			if (client != null) {
				client.close();
				client = null;
			}
		} catch (Exception e) {
		}
	}

	@Override
	public String index(String indexName, String type, String json) {
		IndexResponse response = getClient().prepareIndex(indexName, type).setSource(json).get();
		return response.getId();
	}

	@Override
	public String get(String indexName, String type, String id) {
		GetResponse response = getClient().prepareGet(indexName, type, id).get();
		return response.getSourceAsString();
	}

	@Override
	public void delete(String indexName, String type, String id) {
		getClient().prepareDelete(indexName, type, id).get();
	}

	@Override
	public void update(String indexName, String type, String id, String json) {
		getClient().prepareUpdate(indexName, type, id).setDoc(json);
	}

	@Override
	public String search(String indexName, String json) {
		SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
		SearchTemplateResponse searchResponse = new SearchTemplateRequestBuilder(getClient())
                .setRequest(searchRequest)
                .setScript(json)
                .setScriptType(ScriptType.INLINE)
                .setScriptParams(null)
                .get();
		return searchResponse.toString();
	}

}
