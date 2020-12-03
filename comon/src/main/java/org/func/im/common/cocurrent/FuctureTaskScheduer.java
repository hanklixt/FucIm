package org.func.im.common.cocurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author lxt
 * @create 2020-12-03 17:11
 */
@Slf4j
public class FuctureTaskScheduer  extends Thread{



     private  ConcurrentLinkedQueue taskQueue=    new ConcurrentLinkedQueue<>();



    public FuctureTaskScheduer(){


        this.start();
    }

}
