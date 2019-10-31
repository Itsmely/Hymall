package com.leyou.user.service;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if(type == 1){
            user.setUsername(data);
        }else if(type == 2){
            user.setPhone(data);
        }

        return userMapper.selectCount(user) == 0;
    }
}
