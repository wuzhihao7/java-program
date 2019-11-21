package com.technologysia;

import jdk.dynalink.beans.StaticClass;

import java.io.Serializable;

/**
 * @author house
 */
public class Person implements Serializable {
    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0L, 0L);
    private Long id = snowflakeIdWorker.nextId();
    private String name = "name";
    private Integer age = 27;
    private String gender = "男";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
