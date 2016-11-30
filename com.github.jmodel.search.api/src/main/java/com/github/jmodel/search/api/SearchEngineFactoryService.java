package com.github.jmodel.search.api;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.github.jmodel.search.spi.SearchEngineFactory;

public class SearchEngineFactoryService {

	private static SearchEngineFactoryService service;
	private ServiceLoader<SearchEngineFactory> loader;

	private SearchEngineFactoryService() {
		loader = ServiceLoader.load(SearchEngineFactory.class);
	}

	public static synchronized SearchEngineFactoryService getInstance() {
		if (service == null) {
			service = new SearchEngineFactoryService();
		}
		return service;
	}

	public SearchEngine getEngine(String engineCode) {
		SearchEngine engine = null;

		try {
			Iterator<SearchEngineFactory> engineFactorys = loader.iterator();
			while (engine == null && engineFactorys.hasNext()) {
				SearchEngineFactory engineFactory = engineFactorys.next();
				engine = engineFactory.getEngine(engineCode);
			}
		} catch (ServiceConfigurationError serviceError) {
			engine = null;
			serviceError.printStackTrace();

		}
		return engine;
	}
}
