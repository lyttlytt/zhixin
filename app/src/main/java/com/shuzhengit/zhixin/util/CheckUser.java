package com.shuzhengit.zhixin.util;

import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.bean.User;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/1 15:53
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CheckUser {
    public static User checkUserIsExists() {
        User user = (User) CacheUtils.getCacheManager(APP.getInstance()).getAsObject(CacheKeyManager.USER);
        return user != null ? user : null;
    }

    public static Token checkTokenIsExists(){
//        User user = checkUserIsExists();
//        if (user==null){
//            CacheUtils.getCacheManager(APP.getInstance()).remove(CacheKeyManager.TOKEN);
//            return null;
//        }else {
            Token token = (Token) CacheUtils.getCacheManager(APP.getInstance()).getAsObject(CacheKeyManager.TOKEN);
            return token;
//        }
    }


}
