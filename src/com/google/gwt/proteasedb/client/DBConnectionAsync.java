package com.google.gwt.proteasedb.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBConnectionAsync {
	/**
     * get protease info
     * 
     * @return
	 * @throws Throwable 
     */
    public void getResultbySubstrateInfo(SearchRequest inputObject, AsyncCallback<ResultbySubstrateData[]> callback);

}
