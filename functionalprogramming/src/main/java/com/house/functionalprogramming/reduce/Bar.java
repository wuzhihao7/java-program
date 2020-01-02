package com.house.functionalprogramming.reduce;

import lombok.Data;

import java.util.List;

@Data
public class Bar {
    private String name;
    private Integer count;
    private Double totalTypeValue;
    private List<Baz> bazList;
    public Bar sum(Foo foo){
        if(name == null){
            this.name = foo.getName();
        }
        this.count += foo.getCount();
        this.totalTypeValue += foo.getTypeValue();
        this.bazList.add(new Baz(foo.getType(),foo.getTypeValue()));
        return this;
    }
}
