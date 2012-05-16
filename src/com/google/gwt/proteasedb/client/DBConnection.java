package com.google.gwt.proteasedb.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConnection extends RemoteService {
	/**
     * get protease info
     * 
     * @return
	 * @throws Throwable 
     */
    public CleavagesiteData[] getCleavagesiteInfo(SearchRequest inputObject) throws Throwable;

}
