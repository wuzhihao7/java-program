package com.keehoo.rule.pojo.entity;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/28
 */
@Setter
@Getter
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class Student {
    private int id;
    @NonNull private String name;
    private int age;
}
