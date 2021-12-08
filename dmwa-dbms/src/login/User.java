package login;

import common.Utility;

public class User {

    String username_encrypted;
    String username_plain;
    String password;
    String security_question;
    String security_answer;

    
    public User(String username_encrypted, String username_plain, String password, String security_question,
            String security_answer) {
        this.username_encrypted = username_encrypted;
        this.username_plain = username_plain.toUpperCase();
        if(!Utility.is_not_null_empty(username_encrypted) && Utility.is_not_null_empty(this.username_plain)){
            this.username_encrypted = Cryption.encrypt(this.username_plain);
        }
        this.password = password;
        this.security_question = security_question;
        this.security_answer = security_answer;
    }


    
    /** 
     * @return String
     */
    public String getUsername_encrypted() {
        return username_encrypted;
    }


    
    /** 
     * @param username_encrypted
     */
    public void setUsername_encrypted(String username_encrypted) {
        this.username_encrypted = username_encrypted;
    }

    
    /** 
     * @return String
     */
    public String getUsername_plain() {
        return username_plain;
    }


    
    /** 
     * @param username_plain
     */
    public void setUsername_plain(String username_plain) {
        this.username_plain = username_plain;
    }


    
    /** 
     * @return String
     */
    public String getPassword() {
        return password;
    }


    
    /** 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    
    /** 
     * @return String
     */
    public String getSecurity_question() {
        return security_question;
    }


    
    /** 
     * @param security_question
     */
    public void setSecurity_question(String security_question) {
        this.security_question = security_question;
    }


    
    /** 
     * @return String
     */
    public String getSecurity_answer() {
        return security_answer;
    }


    
    /** 
     * @param security_answer
     */
    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }


    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "User [password=" + password + ", security_answer=" + security_answer + ", security_question="
                + security_question + ", username_encrypted=" + username_encrypted + ", username_plain="
                + username_plain + "]";
    }

    
    
}
