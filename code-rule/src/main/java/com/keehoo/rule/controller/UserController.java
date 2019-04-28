package com.keehoo.rule.controller;

import com.keehoo.rule.dto.ApiResult;
import com.keehoo.rule.dto.UserInputDTO;
import com.keehoo.rule.dto.UserOutputDTO;
import com.keehoo.rule.entity.Student;
import com.keehoo.rule.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ApiResult<UserOutputDTO> addUser(@RequestBody UserInputDTO userInputDTO){
        User user = userInputDTO.convertToUser();
        UserOutputDTO userOutputDTO = new UserOutputDTO().convertFor(user);
        ApiResult<UserOutputDTO> apiResult = new ApiResult<>(userOutputDTO);
        return apiResult;
    }

    @GetMapping
    public Student getStudent(){
        return Student.of("keehoo").setId(1).setAge(12);
    }

}
