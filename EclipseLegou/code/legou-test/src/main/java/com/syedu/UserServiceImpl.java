package com.syedu;

public class UserServiceImpl implements UserService {


    @Override
    public void add() {
        System.out.println("通过构造器手动注入");
    }
}
