package com.fontre.clienthardwaredevice.rest;

import com.alibaba.fastjson.JSONObject;
import com.fontre.clienthardwaredevice.log.AdminLog;
import com.fontre.clienthardwaredevice.service.HardwareDeviceService;
import com.fontre.clienthardwaredevice.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HardwareDeviceController
 * @BelongPackage com.fontre.clienthardwaredevice.rest
 * @Description:
 * @Date: 5/18/22 1:32 PM
 * @Author: PXD
 * @Version V1.1.0
 */
@Api(tags = "一键开关")
@RestController
@RequestMapping(value = "/api/buttonSwitch", produces="application/json;charset=UTF-8")
public class HardwareDeviceController {

    private final HardwareDeviceService hardwareDeviceService;

    public HardwareDeviceController(HardwareDeviceService hardwareDeviceService){
        this.hardwareDeviceService = hardwareDeviceService;
    }

    @GetMapping(value = "switchMode")
    @AdminLog("WEB一键开关")
    @ApiOperation("WEB一键开关")
    public String energyPattern(@RequestParam String sphereUuid, String commandNumber, boolean pattern, String browserUuid, String equipment){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sphereUuid",sphereUuid);
        jsonObject.put("commandNumber",commandNumber);
        jsonObject.put("browserUuid",browserUuid);
        jsonObject.put("equipment",equipment);
//        jsonObject.put("autoRun",pattern);
        jsonObject.put("show",0);
        return JsonUtils.toJson(hardwareDeviceService.controlPtz(jsonObject));
    }
}
