package org.func.im.client.command.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.command.BaseCommand;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @author lxt
 * @create 2020-12-01 11:26
 */
@Slf4j
@Service("chatConsoleCommand")
@Data
public class ChatConsoleCommand implements BaseCommand {


    private String toUserId;

    private String message;

    public static final String KEY="2";


    @Override
    public void exec(Scanner scanner) {
        log.info("请输入聊天的信息(id:message)");
        String[] info=null;
        while (true){
            String message = scanner.next();
            info=message.split(":");
            if (info.length!=2){
                log.info("请按格式输入聊天信息(id:message)");
            }else {
                break;
            }
        }
        toUserId=info[0];
        message=info[1];
    }


    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "聊天";
    }
}
