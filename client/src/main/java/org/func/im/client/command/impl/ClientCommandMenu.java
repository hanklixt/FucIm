package org.func.im.client.command.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.command.BaseCommand;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @author lxt
 * @create 2020-12-01 11:57
 */
@Data
@Slf4j
@Service("clientCommandMenu")
public class ClientCommandMenu implements BaseCommand {

    public static final String KEY="0";


    private String allCommandsShow;

    private String inputCommand;

    @Override
    public void exec(Scanner scanner) {
        log.info("展示所有命令");
        log.info(allCommandsShow);
        inputCommand = scanner.next();
    }


    @Override
    public String getKey() {
        return KEY;
    }


    @Override
    public String getTip() {
        return "展示所有命令";
    }
}
