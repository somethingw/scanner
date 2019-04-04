package com.week.bean.response;

import com.week.bean.base.ResBase;

public class ResLogin extends ResBase {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
