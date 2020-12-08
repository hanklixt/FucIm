package org.func.im.client.command.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.ClientSession;
import org.func.im.client.command.BaseCommand;
import org.func.im.common.bean.User;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @author lxt
 * @create 2020-12-01 11:55
 */
@Slf4j
@Data
@Service("logoutConsoleCommand")
public class LogoutConsoleCommand implements BaseCommand {

    public static final String KEY="10";


    public ClientSession session;

    public User user;


    @Override
    public void exec(Scanner scanner) {
        log.info("退出登录");
        session=null;
        user=null;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "退出登录";
    }
}
