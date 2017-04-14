package com.ws.customerservice.web.controller;

import com.ws.customerservice.config.AppConfig;
import com.ws.customerservice.dto.giftcard.GiftcardDto;
import com.ws.customerservice.service.GiftcardService;
import com.ws.services.commonutil.model.GCRequest;
import com.ws.services.commonutil.model.GCResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



/**
 * ----------------------------------------------------------------------------
 * - Title:  Giftcard Controller
 * - Description:  This is the controller for the Giftcard functionality
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 3/28/16
 * - @version $Rev$
 * -    3/28/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("/giftcard")
public class GiftcardController {

    @Autowired
    private GiftcardService giftcardService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppConfig appConfig;

    @RequestMapping(value = "/shredGC", method = RequestMethod.POST)
    public ResponseEntity<GiftcardDto> shredGiftcard(@RequestParam String gcNumber) {
        log.info("************************************* shredGiftcard");

        GiftcardDto giftcardDto = giftcardService.shredGiftcard(gcNumber);
        if (giftcardDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<GiftcardDto>(giftcardDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public ResponseEntity<GCResponse> getHistoryAndBalance(@RequestBody GCRequest gcRequest) {

        ResponseEntity<GCResponse> responseEntity = null;
        try {

            HttpEntity<GCRequest> httpEntity = new HttpEntity<>(gcRequest);
            responseEntity = restTemplate.postForEntity(appConfig.getGcHistoryUrl(), httpEntity, GCResponse.class);

            log.info("-=[ responseEntity.getStatusCode: {} ]=-", responseEntity.getStatusCode());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Successfully Received Gift Card History");
                ResponseEntity<GCResponse> updatedResponse = new ResponseEntity<GCResponse>(responseEntity.getBody(), HttpStatus.OK);
                return updatedResponse;
            } else {
                log.info("Unable to request Gift Card History");
                return responseEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseEntity;
        }
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public ResponseEntity<GCResponse> activateCard(@RequestBody GCRequest gcRequest) {

        ResponseEntity<GCResponse> responseEntity = null;
        try {
            // set the useEMS to false for testing
            gcRequest.setUseEMS(false);

            HttpEntity<GCRequest> httpEntity = new HttpEntity<>(gcRequest);
            responseEntity = restTemplate.postForEntity(appConfig.getGcActivateUrl(), httpEntity, GCResponse.class);

            log.info("-=[ responseEntity.getStatusCode: {} ]=-", responseEntity.getStatusCode());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Successfully Received Gift Card Activation Response");
                ResponseEntity<GCResponse> updatedResponse = new ResponseEntity<GCResponse>(responseEntity.getBody(), HttpStatus.OK);
                return updatedResponse;
            } else {
                log.info("Unable to activate Gift Card ");
                return responseEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseEntity;
        }
    }

}
