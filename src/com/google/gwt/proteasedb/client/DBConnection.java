package com.google.gwt.proteasedb.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConnection extends RemoteService {
	/**
     * get protease info
     * 
     * @return
	 * @throws Throwable 
     */
    public ResultbySubstrateData[] getResultInfo(SearchRequest[] inputObject) throws Throwable;

}
