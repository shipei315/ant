package com.pine.ant.lambda.customerCollector;

public class StringCombiner {
    
    public StringBuilder sb = new StringBuilder();
    
    public StringCombiner add(String element){
        sb.append(element);
        return this;
    }
    
    public StringCombiner merge(StringCombiner sc){
        sb.append(sc.sb);
        return this;
    }
}
