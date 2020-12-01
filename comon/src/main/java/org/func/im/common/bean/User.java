package org.func.im.common.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxt
 * @create 2020-12-01 15:14
 */
@Data
@Slf4j
public class User {

    //用户id
    private String uid;

    //设备id
    private String devId;

    //用户token
    private String token;

    //用户昵称
    private String nickName="nickName";

    //会话id
    private String sessionId;


    private PLAYTYPE platform=PLAYTYPE.WINDOWS;


    //终端设备
    public  enum PLAYTYPE{
        WINDOWS, MAC, ANDROID, IOS, WEB, OTHER;
    }


    public void setPlatfrom(int platfrom){
        PLAYTYPE[] values = PLAYTYPE.values();
        for (PLAYTYPE value : values) {
            if (value.ordinal()==platfrom){
                this.platform=value;
                break;
            }
        }

    }












}
