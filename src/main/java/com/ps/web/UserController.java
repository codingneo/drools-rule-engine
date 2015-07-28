package com.ps.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps.dao.DBManager;
import com.ps.dao.TestDBManagerStub;
import com.ps.entity.User;


@RestController
public class UserController {
	
	private DBManager dbManager = new TestDBManagerStub();
	
    @RequestMapping("/user")
    public User getUser(@RequestParam(value="id") String userId) {
       if(userId==null || userId.trim().length()==0){
    	   throw new RuntimeException("User Id is empty!");
       }
       
       return dbManager.retreiveUser(userId);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<String> updateUser(@RequestBody User user) {
       User user1 = dbManager.retreiveUser(user.getId());
       if(user1==null){
    	   throw new RuntimeException("User does not exist!");
       }
       
       System.out.println("Inside updateUser: "+user);
       
       dbManager.updateUser(user);
       
       return new ResponseEntity<String>(HttpStatus.OK);
    }   
    
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public ResponseEntity<String> clear() {
       dbManager.clearTransactions();
       dbManager.resetUsers();
       return new ResponseEntity<String>(HttpStatus.OK);
    }   
}
