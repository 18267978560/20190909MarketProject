package com.syedu;


public class UserController {

	//通过构造器手动注入
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;


    }
    
    public void addUser(){
        userService.add();
    }
}
