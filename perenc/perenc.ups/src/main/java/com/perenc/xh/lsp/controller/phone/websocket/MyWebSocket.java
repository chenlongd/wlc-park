package com.perenc.xh.lsp.controller.phone.websocket;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket/{username}")
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识（ 这里就使用的Map实现一对一和一对多）

    //private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

    private static Map<String, MyWebSocket> clients = new ConcurrentHashMap<String, MyWebSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //单个连接的名称，及其session的关键字
    private String username;
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        this.username = username;
        this.session = session;
        addOnlineCount();  //在线数加1
        clients.put(username, this);  //将webSocket的连接存在map中，通过用户名来指定对应的session
        System.out.println(username+"已连接"+session.getId()+"在线人数"+ getOnlineCount());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();  //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public static void onMessage(String message,Session session) throws IOException {  //message是服务器收到的消息json串；里面包括发给说，发的什么

        System.out.println("来自客户端的消息:" + message);
        JSONObject jsonTo = JSONObject.parseObject(message);
        if (!jsonTo.get("To").equals("All")){  //一对一发
            sendMessageTo("给一个人说："+jsonTo.get("message").toString(), jsonTo.get("To").toString());
        }else{  //群发
            sendMessageAll("给所有人说："+jsonTo.get("message").toString());
        }
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    /**
     * 发给某一个人。
     * @param message
     * @throws IOException
     */
    public static void sendMessageTo(String message, String To) throws IOException {
        // session.getBasicRemote().sendText(message);
        //session.getAsyncRemote().sendText(message);
        for (MyWebSocket item : clients.values()) {
            if (item.username.equals(To) ) {
                item.session.getAsyncRemote().sendText(To + "---" + message);
            }
        }
    }
    //群发
    public static void sendMessageAll(String message) throws IOException {
        for (MyWebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }
    //保证onlineCount的线程安全，使用同步锁
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

    public static synchronized Map<String, MyWebSocket> getClients() {
        return clients;
    }

}