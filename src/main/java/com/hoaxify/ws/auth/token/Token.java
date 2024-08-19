package com.hoaxify.ws.auth.token;

public class Token {
    String token;
    String prefix;
    

    public Token( String prefix, String token) {
        this.token = token;
        this.prefix = prefix;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
     
}
