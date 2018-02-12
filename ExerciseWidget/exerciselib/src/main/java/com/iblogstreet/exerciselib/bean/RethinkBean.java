package com.iblogstreet.exerciselib.bean;

/**
 * 类描述：反思组件Bean
 * 创建人：王军
 * 创建时间：2018/1/17
 */

public class RethinkBean {
    private int id;
    private String name;

    public RethinkBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name:" + this.name + ",id:" + this.id;
    }

    public boolean equals(RethinkBean obj) {
        return this.id == obj.id;
    }
}
