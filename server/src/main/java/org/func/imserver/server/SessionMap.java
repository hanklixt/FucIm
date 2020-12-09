package org.func.imserver.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.User;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lxt
 * @create 2020-12-09 16:48
 */
@Data
@Slf4j
public class SessionMap {

    private final static Map<String,ServerSession> map=new ConcurrentHashMap<>();

    private SessionMap(){

    }

    private static class SessionMapInstance{

        private final static SessionMap inst=new SessionMap();


    }

    public SessionMap getInst(){
        return SessionMapInstance.inst;
    }

    //加入会话
    public void addSession(String sessionId,ServerSession session){
        map.put(sessionId,session);
        log.info("用户:"+session.getUser().getUid()+"上线");
        log.info("当前在线人数:"+map.size());
    }

    //移除会话
    public void removeSession(String sessionId){

        ServerSession serverSession = map.get(sessionId);

        if (serverSession==null){
            return;
        }

        String uid = serverSession.getUser().getUid();

        log.info("用户:"+uid+"下线了");

        map.remove(sessionId);
    }

    //key 获取
    public ServerSession getSession(String sessionId){
       return   map.get(sessionId);
    }

    /**
     * 用户可能会多端登录
     * @return
     */
    public List<ServerSession> getSessionBy(String userId){
        return map.values()
                .stream()
                .filter(x -> userId.equals(x.getUser().getUid()))
                .collect(Collectors.toList());
    }

    /**
     * 是否登录
     * @param user
     * @return
     */
    public boolean isLogin(User user){
        Iterator<Map.Entry<String, ServerSession>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){

            Map.Entry<String, ServerSession> next = iterator.next();
            ServerSession value = next.getValue();
            if (value.getUser().getUid().equals(user.getUid())){
                return true;
            }
        }
        return false;
    }










}
