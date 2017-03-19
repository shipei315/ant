package com.pine.ant.lambda.inherit;

public interface Tool2 {

    public default String work() {
        return "tool2 ***";
    }
}
