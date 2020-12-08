package org.func.im;

import org.func.im.client.CommandController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author lxt
 * @create 2020-12-08 16:56
 */
@SpringBootApplication
@EnableAutoConfiguration
public class ClientApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(ClientApplication.class, args);

        CommandController commandController = (CommandController)run.getBean("commandController");

        //初始化
        commandController.initCommandMap();
        try {
            commandController.startCommandThread();
        }catch (Exception e){
            e.printStackTrace();
        }
        

    }

}
