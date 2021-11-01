package com.streamlet.db;

import com.streamlet.db.client.Id;
import com.streamlet.db.client.Length;
import com.streamlet.db.client.Table;

@Table(name = "stu_user")
public class User {
    @Id()
    private int id;
    @Length(value = 20)
    private String name;

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
}
