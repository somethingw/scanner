package com.week.bean.request;
/**
 * 登录结构体
 */

import com.week.bean.base.ReqBase;

public class ReqLogin extends ReqBase {
    private Params params;

    public ReqLogin(String username, String password) {
        super();
        params=new Params();
        params.setUserName(username);
        params.setPassWord(password);
    }


    public class Params {
        private String userName;
        private String passWord;
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getUserName() {
            return userName;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }
        public String getPassWord() {
            return passWord;
        }

    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}
