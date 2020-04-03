package com.normal.gmail20.logger.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.normal.gmail20.constants.GmailConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
public class LoggerController {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @ResponseBody
    public String log(@RequestBody String logString){

        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("currentTime", System.currentTimeMillis());

        log.info(logString);
        if ("startup".equals(jsonObject.get("type"))) {
            kafkaTemplate.send(GmailConstant.KAFKA_TOPIC_STARTUP,jsonObject.toJSONString());
        }else {
            kafkaTemplate.send(GmailConstant.KAFKA_TOPIC_EVENT,jsonObject.toJSONString());
        }


        System.out.println(logString);
        return "success";

    }
}
