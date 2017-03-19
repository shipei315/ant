package com.pine.ant.lambda.inherit;

/**
 * 多重继承示例代码
 * @author shipei
 *
 */
public class ChildTool implements Tool1, Tool2{

    // 该方法如果不明确的指明是覆盖哪个接口的方法将报错
    @Override
    public String work() {
        // TODO Auto-generated method stub
        return Tool1.super.work();
    }

}
