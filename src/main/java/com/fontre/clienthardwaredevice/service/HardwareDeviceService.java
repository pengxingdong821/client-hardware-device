package com.fontre.clienthardwaredevice.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.fontre.clienthardwaredevice.cons.RedisKeyCons;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HardwareDeviceService
 * @BelongPackage com.fontre.clienthardwaredevice.service
 * @Description:
 * @Date: 5/12/22 8:02 PM
 * @Author: PXD
 * @Version V1.1.0
 */

@Component
public class HardwareDeviceService {

    private Socket client;	//定义客户端套接字


    /***
     * 一键开关机轮训进来执行命令开关机，需要socket连接设备
     *    执行完成命令之后返回消息到  /api/buttonSwitch/clientReturnState
     * @return
     */
    public String onekeySwitch(Map<String, Object> data){
        if(!data.get("commandNumber").toString().equals("")){
            this.getClient(data.get("commandNumber").toString());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sphereUuid",data.get("sphereUuid").toString());
//            jsonObject.put("autoRun",data.get("autoRun").toString());
            jsonObject.put("commandNumber",data.get("commandNumber").toString());
            String operation = HttpRequest.post("http://iduacarshow.fontre.com:6301/api/buttonSwitch/clientReturnState")
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(jsonObject.toString())
                    .execute()
                    .body();
        }
        return null;
    }


    public Map<String,String> controlPtz(com.alibaba.fastjson.JSONObject data) {

        Map<String, String> ret = new HashMap<String, String>();
        ret.put("show", "1");
        String sphereUuid = data.getString("sphereUuid");
//        String commandNumber = data.getString("commandNumber");
//        String pattern = data.getString("autoRun");
        String equipment = data.getString("equipment");
        String browserUuid = data.getString("browserUuid");
        return null;
    }


    //建立客户端函数
    public void getClient(String printText){
        try {
//            client = new Socket("127.0.0.1", 1100);	//建立客户端，使用的IP为127.0.0.1，端口和服务器一样为1100
            client = new Socket("192.168.16.110", 12345);	//建立客户端，使用的IP为127.0.0.1，端口和服务器一样为1100
            System.out.println("客户端建立成功！");

            setClientMessage(printText);		//调用客户端信息写入函数
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static byte[] hexStrToByteArrs(String hexString) {
//        if (StringUtils.isEmpty(hexString)) {
//            return null;
//        }

        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;

        byte[] bytes = new byte[len / 2];

        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }

        return bytes;
    }


    //定义客户端信息写入函数
    public void setClientMessage(String printText){
        try {
            OutputStream pt = client.getOutputStream();		//建立客户端信息输出流
//            String printText = "服务器你好！我是客户端！";
//            String printText = close_hub;  //open_hub,close_hub,open_pc,close_pc
//            pt.write(printText.getBytes());		//以二进制的形式将信息进行输出
//            System.out.println("printText after hexStrToByteArrs:");
//            System.out.println(hexStrToByteArrs(printText));
//            System.out.println(Arrays.toString(hexStrToByteArrs(printText)));
            pt.write(hexStrToByteArrs(printText));		//以二进制的形式将信息进行输出

            InputStream input = client.getInputStream();	//建立客户端信息输入流
            byte [] b = new byte[1024];		//定义字节数组
            int len = input.read(b);	//读取接收的二进制信息流
            String data = new String(b, 0,len);
            System.out.println("收到服务器消息：" + data);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            //如果客户端信息流不为空，则说明客户端已经建立连接，关闭客户端
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
