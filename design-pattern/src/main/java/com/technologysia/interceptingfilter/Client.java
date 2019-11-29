package com.technologysia.interceptingfilter;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/18
 */
public class Client {
    private FilterManager filterManager;

    public void setFilterManager(FilterManager filterManager){
        this.filterManager = filterManager;
    }

    public void sendRequest(String request){
        filterManager.filterRequest(request);
    }
}
