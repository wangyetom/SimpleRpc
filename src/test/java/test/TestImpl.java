package test;

import annotation.Service;

/**
 * Created by wangye on 17/12/11.
 */
@Service
public class TestImpl implements TestInterface{
    public String helloWorld(String name){
        return "hello"+name;
    }
}
