/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class Student {
    private int id, userId; private String fullName, phoneNumber, emailAddress, address; private Date dateOfBirth;
    public Student(){}
    public Student(int id,int userId,String n,String p,String e,Date d,String a){
        this.id=id; this.userId=userId; this.fullName=n; this.phoneNumber=p; this.emailAddress=e; this.dateOfBirth=d; this.address=a;
    }
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public int getUserId(){return userId;} public void setUserId(int userId){this.userId=userId;}
    public String getFullName(){return fullName;} public void setFullName(String n){this.fullName=n;}
    public String getPhoneNumber(){return phoneNumber;} public void setPhoneNumber(String p){this.phoneNumber=p;}
    public String getEmailAddress(){return emailAddress;} public void setEmailAddress(String e){this.emailAddress=e;}
    public Date getDateOfBirth(){return dateOfBirth;} public void setDateOfBirth(Date d){this.dateOfBirth=d;}
    public String getAddress(){return address;} public void setAddress(String a){this.address=a;}
}


