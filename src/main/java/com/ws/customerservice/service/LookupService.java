package com.ws.customerservice.service;

import com.ws.customerservice.dao.LookupDao;
import com.ws.customerservice.model.LookupDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  Lookup Service
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.service
 * - @date: 9/7/16
 * - @version $Rev$
 * -    9/7/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Service
public class LookupService {

    @Autowired
    private LookupDao lookupDao;

    public List<LookupDto> getStateList() {

        List<LookupDto> lookupDtoList = lookupDao.daoGetStates();
        return lookupDtoList;
    }
}
