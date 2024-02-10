package com.equipo.SignUp;

public class UserHelperClass {

    String FullName, Username,Email,PhoneNo,Password,Date,Gender,PhoneNoComplete;

  //  public UserHelperClass(){}

    public UserHelperClass(String fullName, String username, String email, String phoneNo, String password, String date, String gender, String phoneNoComplete) {
        FullName = fullName;
        Username = username;
        Email = email;
        PhoneNo = phoneNo;
        Password = password;
        Date = date;
        Gender = gender;
        PhoneNoComplete = phoneNoComplete;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhoneNoComplete() {
        return PhoneNoComplete;
    }

    public void setPhoneNoComplete(String phoneNoComplete) {
        PhoneNoComplete = phoneNoComplete;
    }
}
