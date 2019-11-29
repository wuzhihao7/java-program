package com.technologysia.interceptingfilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/18
 */
public class FilterChain {
    private List<Filter> filters = new ArrayList<>();
    private Target target;

    public void addFilter(Filter filter){
        filters.add(filter);
    }

    public void setTarget(Target target){
        this.target = target;
    }

    public void execute(String request){
        for(Filter filter:filters){
            filter.execute(request);
        }
        target.execute(request);
    }
}
