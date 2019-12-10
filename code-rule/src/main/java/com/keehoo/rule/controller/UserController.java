package com.technologysia.rule.controller;

import com.technologysia.rule.pojo.vo.ApiResult;
import com.technologysia.rule.pojo.dto.UserInputDTO;
import com.technologysia.rule.pojo.dto.UserOutputDTO;
import com.technologysia.rule.pojo.entity.Student;
import com.technologysia.rule.pojo.entity.User;
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
    public ApiResult<Student> getStudent(){
        Student student = Student.of("keehoo").setId(1).setAge(12);
        return new ApiResult<>(student);
    }

}
