package com.keehoo.interceptingfilter;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/18
 */
public class FilterManager {
    private FilterChain filterChain;

    public FilterManager(Target target){
        filterChain = new FilterChain();
        filterChain.setTarget(target);
    }

    public void setFilter(Filter filter){
        filterChain.addFilter(filter);
    }

    public void filterRequest(String request){
        filterChain.execute(request);
    }
}
