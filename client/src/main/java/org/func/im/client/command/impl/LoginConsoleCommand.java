package org.func.im.client.command.impl;

import lombok.Data;
import org.func.im.client.command.BaseCommand;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @author lxt
 * @create 2020-12-01 9:58
 */
@Service("loginConsoleCommand")
@Data
public class LoginConsoleCommand implements BaseCommand {

    public final  static String KEY="1";

    public String userName;

    public String password;


    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入用户名信息(用户名@密码)");
        String[] info=null;
        while (true){
            String input = scanner.next();
            info = input.split("@");
            if (info.length!=2){
                System.out.println("请按格式输入用户名@密码");
            }else {
                break;
            }
        }
        userName=info[0];
        password=info[1];

    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }

}
