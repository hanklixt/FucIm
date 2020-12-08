package org.func.im.common.cocurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lxt
 * @create 2020-12-03 17:11
 */
@Slf4j
public class FutureTaskScheduler extends Thread{

     private final  static ConcurrentLinkedQueue<ExecuteTask> taskQueue= new ConcurrentLinkedQueue<ExecuteTask>();

     private final static ExecutorService executor= Executors.newFixedThreadPool(5);

     private final static FutureTaskScheduler inst=new FutureTaskScheduler();

    /**
     * 线程睡眠事件--单位毫秒
     */
    private long sleepTime=2000;

     private FutureTaskScheduler(){
        this.start();
    }


    @Override
    public void run() {
        handleTask();
        try{
            Thread.sleep(sleepTime);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


    /**
     * 加入任务队列
     * @param executeTask
     */
    public static void add(ExecuteTask executeTask){
         inst.taskQueue.add(executeTask);
    }


    //检查队列中是否有任务，有任务就取出来处理掉
    public void handleTask(){
        try {
            while (taskQueue.peek()!=null){
                ExecuteTask executeTask = taskQueue.poll();
                handleTask(executeTask);
            }
        }catch (Exception e){
           log.error(e.getMessage());
        }


    }


    public void handleTask(ExecuteTask executeTask){
        ExecuteRunnable runnable = new ExecuteRunnable(executeTask);
        executor.execute(runnable);
    }


    class ExecuteRunnable implements Runnable{

        private ExecuteTask executeTask;

        public ExecuteRunnable(ExecuteTask executeTask) {
            this.executeTask = executeTask;
        }

        @Override
        public void run() {
             executeTask.execute();
        }
    }


}
