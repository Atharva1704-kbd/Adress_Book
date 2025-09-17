/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

public class User {
    private int id; private String username; private String password;
    public User(){}
    public User(int id, String u, String p){ this.id=id; this.username=u; this.password=p; }
    public int getId(){ return id; } public void setId(int id){ this.id=id; }
    public String getUsername(){ return username; } public void setUsername(String u){ this.username=u; }
    public String getPassword(){ return password; } public void setPassword(String p){ this.password=p; }
}


