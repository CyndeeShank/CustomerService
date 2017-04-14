package com.ws.customerservice.web.controller;

import com.ws.customerservice.model.LookupDto;
import com.ws.customerservice.service.LookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  Lookup Controller
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 9/7/16
 * - @version $Rev$
 * -    9/7/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("/lookup")
public class LookupController {

    @Autowired
    private LookupService lookupService;

    @RequestMapping(value="/states")
    public ResponseEntity<List<LookupDto>> getStates() {
        log.info("************************************* lookup-getStates");

        List<LookupDto> statesList = lookupService.getStateList();

        if (statesList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(statesList, HttpStatus.OK);
        }
    }
    @RequestMapping(value="/sizes")
    public ResponseEntity<List<LookupDto>> getSizes() {
        log.info("************************************* lookup-getSizes");

        List<LookupDto> sizeList = new ArrayList();
        LookupDto lookupDto = new LookupDto();
        lookupDto.setAbbreviation("W-1");
        lookupDto.setDescription("Womens - One Size");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("M-M");
        lookupDto.setDescription("Mens - Medium");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("M-L");
        lookupDto.setDescription("Mens - Large");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("M-XL");
        lookupDto.setDescription("Mens - Extra Large");
        sizeList.add(lookupDto);

        if (sizeList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(sizeList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/quantity")
    public ResponseEntity<List<LookupDto>> getQuantity() {
        log.info("************************************* lookup-getQuantity");

        List<LookupDto> sizeList = new ArrayList();
        LookupDto lookupDto = new LookupDto();
        lookupDto.setAbbreviation("1");
        lookupDto.setDescription("One");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("2");
        lookupDto.setDescription("Two");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("3");
        lookupDto.setDescription("Three");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("4");
        lookupDto.setDescription("Four");
        sizeList.add(lookupDto);
        lookupDto = new LookupDto();
        lookupDto.setAbbreviation("5");
        lookupDto.setDescription("Five");
        sizeList.add(lookupDto);

        if (sizeList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(sizeList, HttpStatus.OK);
        }
    }

}
