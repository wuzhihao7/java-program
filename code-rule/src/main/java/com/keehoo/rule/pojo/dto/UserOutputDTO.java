package com.keehoo.rule.pojo.dto;

import com.keehoo.rule.converter.Converter;
import com.keehoo.rule.pojo.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/20
 */
@Data
public class UserOutputDTO {
    private String name;
    private int age;

    public UserOutputDTO convertFor(User user){
        UserOutputDTOConverter userOutputDTOConverter = new UserOutputDTOConverter();
        return userOutputDTOConverter.doBackward(user);
    }

    private static class UserOutputDTOConverter implements Converter<UserOutputDTO, User>{

        @Override
        public User doForward(UserOutputDTO userOutputDTO) {
            throw new AssertionError("不支持逆向转化方法!");
        }

        @Override
        public UserOutputDTO doBackward(User user) {
            UserOutputDTO userOutputDTO = new UserOutputDTO();
            BeanUtils.copyProperties(user, userOutputDTO);
            return userOutputDTO;
        }
    }
}
