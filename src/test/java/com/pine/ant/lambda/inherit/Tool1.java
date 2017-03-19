package com.pine.ant.lambda.inherit;

public interface Tool1 {
    
    public default String work() {
        return "tool1 ***";
    }
}
