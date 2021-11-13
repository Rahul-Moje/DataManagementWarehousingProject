package login;

import common.Utility;

public class UserLoginDetails {

    String username_encrypted;
    String username_plain;
    String password;
    String security_question;
    String security_answer;

    
    public UserLoginDetails(String username_encrypted, String username_plain, String password, String security_question,
            String security_answer) {
        this.username_encrypted = username_encrypted;
        this.username_plain = username_plain;
        if(Utility.is_null_empty(username_encrypted) && !Utility.is_null_empty(this.username_plain)){
            this.username_encrypted = Cryption.encrypt(username_encrypted);
        }
        this.password = password;
        this.security_question = security_question;
        this.security_answer = security_answer;
    }


    public String getUsername_encrypted() {
        return username_encrypted;
    }


    public void setUsername_encrypted(String username_encrypted) {
        this.username_encrypted = username_encrypted;
    }

    public String getUsername_plain() {
        return username_plain;
    }


    public void setUsername_plain(String username_plain) {
        this.username_plain = username_plain;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getSecurity_question() {
        return security_question;
    }


    public void setSecurity_question(String security_question) {
        this.security_question = security_question;
    }


    public String getSecurity_answer() {
        return security_answer;
    }


    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    
    
}
