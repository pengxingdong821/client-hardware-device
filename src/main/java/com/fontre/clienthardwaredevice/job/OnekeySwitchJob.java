package com.fontre.clienthardwaredevice.job;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.fontre.clienthardwaredevice.cons.RedisKeyCons;
import com.fontre.clienthardwaredevice.service.HardwareDeviceService;
import com.fontre.clienthardwaredevice.util.RedisUtils;
import org.json.simple.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fontre.clienthardwaredevice.handler.GlobalExceptionHandler;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OnekeySwitchJob
 * @BelongPackage com.fontre.clienthardwaredevice.job
 * @Description: 一键开关机
 * @Date: 5/12/22 5:32 PM
 * @Author: PXD
 * @Version V1.1.0
 */
@Component
public class OnekeySwitchJob {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${fontre.FileUrl}")
    private String fileUrl;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HardwareDeviceService hardwareDeviceService;

//    @Scheduled(cron = "0/3 * * * * ? ")
    public void fixedDelay(){
        if(redisUtils.hasKey(RedisKeyCons.SPHERESUUID)){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sphereUuid",redisUtils.get(RedisKeyCons.SPHERESUUID));
            String operation = HttpRequest.post("http://iduacarshow.fontre.com:6301/api/buttonSwitch/clientObtainOperation")
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(jsonObject.toString())
                    .execute()
                    .body();
            Map<String, Object> open = JSON.parseObject(operation);
            Map<String, Object> data = JSON.parseObject(open.get("data").toString());
            if(data.get("show").toString().equals("0")){
                System.out.println(data.get("show").toString());
                System.out.println(operation);
                hardwareDeviceService.onekeySwitch(data);
            }else {
                log.info("无状态："+operation);
            }
        }else{
            log.info("配置设备redis门店uuid为空！");
        }
    }


//    @Scheduled(cron = "0/5 * * * * ? ")
    public void strategyMode(){
        if(redisUtils.hasKey(RedisKeyCons.SPHERESUUID)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sphereUuid", redisUtils.get(RedisKeyCons.SPHERESUUID));
            String operation = HttpRequest.post("http://iduacarshow.fontre.com:6301/api/energy/consumption/controlStatus")
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(jsonObject.toString())
                    .execute()
                    .body();
            Map<String, Object> open = JSON.parseObject(operation);
            Map<String, Object> data = JSON.parseObject(open.get("data").toString());
            if(data.get("show").toString().equals("0")){

            }else{
                String mode = HttpRequest.post("http://iduacarshow.fontre.com:6301/api/energy/consumption/findByMode?sphereUuid="+redisUtils.get(RedisKeyCons.SPHERESUUID))
                        .header(Header.CONTENT_TYPE, "application/json")
                        .execute()
                        .body();
                Map<String, Object> modeMap = JSON.parseObject(mode);
                List<Map<String, Object>> dataMap = JSON.parseObject(modeMap.get("data").toString(),List.class);
                for (Map<String, Object> maps:dataMap){
                    if(maps.get("switchs").toString().equals("1")){

                    }
                }
            }
        }else{
            log.info("配置设备redis门店uuid为空！");
        }
    }

//    @Scheduled(cron = "0 0/2 * * * ? ")
    public void readJSON(){
        File file = new File(fileUrl);
        if(file.exists()){
            if(redisUtils.hasKey(RedisKeyCons.SPHERESUUID)){
                log.info("redis门店uuid："+redisUtils.get(RedisKeyCons.SPHERESUUID));
            }else{
                JSONParser jsonParser = new JSONParser();
                try {
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(fileUrl));
                    String uuid = (String) jsonObject.get("sphereUuid");
                    redisUtils.set(RedisKeyCons.SPHERESUUID,uuid);
                    System.out.println(uuid);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }else{
            log.info("文件不存在");
        }
    }

//    public void findCollocation(){
//        if(redisUtils.hasKey(RedisKeyCons.SPHERESUUID)){
//            String operation = HttpRequest.post("/api/collocation/findSonCollocation?sphereUuid="+redisUtils.get(RedisKeyCons.SPHERESUUID))
//                    .header(Header.CONTENT_TYPE, "application/json")
//                    .execute()
//                    .body();
//        }
//    }
    public String redFile(){
        File file = new File("/Users/peng/Desktop/sphere.json");
        if(file.exists()){
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("/Users/peng/Desktop/sphere.json"));
                return jsonObject.toJSONString();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        String stop = redFile();
//        if(stop !=null){
//            Map<String, Object> maps = JSON.parseObject(stop);
//            System.out.println(maps.get("sphereUuid").toString());
//        }else{
//            System.out.println("www");
//        }
//    }

}
