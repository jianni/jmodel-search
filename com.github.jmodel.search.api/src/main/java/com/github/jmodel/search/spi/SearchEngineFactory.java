package com.github.jmodel.search.spi;

import com.github.jmodel.search.api.SearchEngine;

public interface SearchEngineFactory {

	public SearchEngine getEngine(String engineCode);
}
