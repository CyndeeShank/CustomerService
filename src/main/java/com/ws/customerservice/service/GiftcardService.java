package com.ws.customerservice.service;

import com.ws.customerservice.dao.GiftcardDao;
import com.ws.customerservice.dto.giftcard.GiftcardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.service
 * - @date: 3/28/16
 * - @version $Rev$
 * -    3/28/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Service
public class GiftcardService {

    @Autowired
    private GiftcardDao giftcardDao;

    public GiftcardDto shredGiftcard(String gcNumber) {

        // call daoShredGiftcard to call proc to shred the gift card
        GiftcardDto giftcardDto = giftcardDao.daoShredGiftcard(gcNumber);

        return giftcardDto;
    }
}
