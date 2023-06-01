package cn.shadowkylin.ham.common;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @创建人 li cong
 * @创建时间 2023/5/16
 * @描述
 */

@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocket {
    private Session session;
    private static final ConcurrentHashMap<String, WebSocket> webSocketConcurrentHashMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value="sid")String sid) {
        this.session = session;
        webSocketConcurrentHashMap.put(sid, this);
        System.out.println("有新连接加入！当前在线人数为" + webSocketConcurrentHashMap.size()+",sid:"+sid);
    }

    @OnClose
    public void onClose(@PathParam(value="sid")String sid) {
        if(webSocketConcurrentHashMap.get(sid)!=null){
            webSocketConcurrentHashMap.remove(sid);
            System.out.println("有一连接关闭！当前在线人数为" + webSocketConcurrentHashMap.size()+",sid:"+sid);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        if(message.indexOf("sid:")==0){
            String sid=message.substring(message.indexOf("sid:")+4,message.indexOf(";"));
            String msg=message.substring(message.indexOf(";")+1);
            WebSocket webSocket=webSocketConcurrentHashMap.get(sid);
            //如果session存在
            if(webSocket.getSession()==session){
                webSocket.sendMessageToUser(msg,sid);
            }
        }
        else{
            sendMessageToAll(message);
        }
    }

    public void sendMessageToUser(String sid,String msg){
        try{
            if(webSocketConcurrentHashMap.get(sid)!=null){
                webSocketConcurrentHashMap.get(sid).getSession().getBasicRemote().sendText(msg);
                System.out.println("发送消息成功");
            }
            else{
                System.out.println("连接未建立，发送消息失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAll(String message) {
        for (WebSocket item : webSocketConcurrentHashMap.values()) {
            try {
                item.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("群发消息成功");
    }

    public Session getSession() {
        return session;
    }
}
