package com.gtalent.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //如果db 有auto_increment  就要這行
    private int id;

    //username作為帳號
    @Column(name="username")
    private String username;
    @Column(name="email")
    private String email;

    //todo 實際應用環境切勿使用明碼儲存
    @Column(name="pwd")
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User(){
        this.id = id;
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }
    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
