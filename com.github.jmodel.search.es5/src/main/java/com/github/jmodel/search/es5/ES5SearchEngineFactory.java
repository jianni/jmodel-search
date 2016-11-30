package com.github.jmodel.search.es5;

import com.github.jmodel.search.api.SearchEngine;
import com.github.jmodel.search.spi.SearchEngineFactory;

public class ES5SearchEngineFactory implements SearchEngineFactory {

	@Override
	public SearchEngine getEngine(String engineCode) {
		if (engineCode.equals("ES5")) {
			return ES5SearchEngine.getInstance();
		}
		return null;
	}

}
