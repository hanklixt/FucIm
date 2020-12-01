package org.func.im.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-01 11:02
 */
@Slf4j
@AllArgsConstructor
@Service("commandController")
public class CommandController {


    /**
     * 启动命令线程
     */
    public void startCommandThread(){

        Thread.currentThread().setName("主线程");




    }

}
