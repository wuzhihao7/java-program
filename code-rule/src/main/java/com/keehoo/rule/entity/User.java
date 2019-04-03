package com.keehoo.rule.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/19
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private LocalDateTime createTime;
    private LocalDateTime alterTime;
}
