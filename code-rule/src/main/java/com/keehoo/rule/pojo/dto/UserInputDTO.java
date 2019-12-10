package com.technologysia.rule.pojo.dto;

import com.technologysia.rule.converter.Converter;
import com.technologysia.rule.pojo.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/20
 */
@Data
public class UserInputDTO {
    private String name;
    private int age;

    public User convertToUser(){
        UserInputDTOConverter userInputDTOConverter = new UserInputDTOConverter();
        User user = userInputDTOConverter.doForward(this);
        return user;
    }

    public UserInputDTO convertFor(User user){
        UserInputDTOConverter userInputDTOConverter = new UserInputDTOConverter();
        UserInputDTO userInputDTO = userInputDTOConverter.doBackward(user);
        return userInputDTO;
    }

    private static class UserInputDTOConverter implements Converter<UserInputDTO, User>{

        @Override
        public User doForward(UserInputDTO userInputDTO) {
            User user = new User();
            BeanUtils.copyProperties(userInputDTO, user);
            return user;
        }

        @Override
        public UserInputDTO doBackward(User user) {
            throw new AssertionError("不支持逆向转化方法!");
        }
    }
}