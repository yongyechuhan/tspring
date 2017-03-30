package com.changingpay.threadtest;

import com.changingpay.tspring.business.SearchTicket;
import com.changingpay.tspring.message.TicketDTO;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Created by Liuxin on 2017/3/30.
 */
public class SearchTicketTest {

    private Log logger = LogFactory.getLog(SearchTicket.class);

    @Test
    public void testSearchTikcet(){
        String filepath = "E:\\cer\\12306key.keystore";
        String keypasswd = "152346";
        String severResponse = SearchTicket.sendByHttps(filepath, keypasswd, "https://kyfw.12306.cn/otn/leftTicket/queryX?" +
                "leftTicketDTO.train_date=2017-03-30&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=SHH&purpose_codes=ADULT");
        logger.info(severResponse);
        JSONObject jsonObject = JSONObject.fromObject(severResponse);
        JSONArray array = (JSONArray) jsonObject.get("data");
        for(int i = 0; i < array.size(); i++){
            JSONObject object = (JSONObject) array.get(i);
            TicketDTO ticketDTO = (TicketDTO) JSONObject.toBean(object, TicketDTO.class);
            logger.info(new Gson().toJson(ticketDTO));
        }
    }
}
