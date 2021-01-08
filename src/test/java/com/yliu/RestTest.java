package com.yliu;

import com.yliu.bean.BaseResult;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RestTest {

    @Test
    public void test(){
        RestTemplate restTemplate = new RestTemplate();

        Map<String,String> param = new HashMap<>();
        String logId = UUID.randomUUID().toString();

        param.put("task_id","123");
        param.put("log_id",logId);
        String url = String.format("http://akshare_server:10002/fundvalue?task_id=123&log_id=%s",logId);

        BaseResult result = restTemplate.getForObject(url, BaseResult.class);
    }
}
