package com.leyou.user.controller;

import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity checkData(@PathVariable("data") String data, @PathVariable("type") Integer type){

        Boolean bool = this.userService.checkUser(data,type);

        if(bool == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(bool);
    }

}
