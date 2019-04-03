package com.keehoo.rule.controller;

import com.keehoo.rule.dto.UserInputDTO;
import com.keehoo.rule.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/19
 */
@RestController
@RequestMapping("/v1/api/user")
@Slf4j
public class UserController {
    @PostMapping
    public User addUser(@RequestBody UserInputDTO userInputDTO){
        log.info("{}", userInputDTO);
        User user = userInputDTO.convertToUser();
        return user;
    }
}
