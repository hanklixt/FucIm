package org.func.im.client.command;

import java.util.Scanner;

/**
 * @author lxt
 * @create 2020-12-01 9:54
 */
public interface BaseCommand {

    void exec(Scanner scanner);

    String getKey();

    String getTip();



}
