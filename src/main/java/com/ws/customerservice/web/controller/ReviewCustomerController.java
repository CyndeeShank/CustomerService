package com.ws.customerservice.web.controller;

import com.ws.customerservice.dto.review.DatatypeDto;
import com.ws.customerservice.dto.review.ReviewCustomerDto;
import com.ws.customerservice.model.CustomerServiceException;
import com.ws.customerservice.service.ReviewCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 8/4/16
 * - @version $Rev$
 * -    8/4/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("/reviewcustomer")
public class ReviewCustomerController {

    @Autowired
    private ReviewCustomerService reviewCustomerService;

    @RequestMapping(value = "/list")
    public ResponseEntity<List<ReviewCustomerDto>> getReviewCustomerList() {
        log.info("************************************* reviewcustomer-list");

        List<ReviewCustomerDto> reviewCustomerDtoList = reviewCustomerService.getReviewFraudList();

        if (reviewCustomerDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reviewCustomerDtoList, HttpStatus.OK);
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ReviewCustomerDto> saveReviewCustomer(@RequestBody @Valid ReviewCustomerDto reviewCustomerDto) {

        ReviewCustomerDto updatedReviewCustomerDto = null;
        try {
            updatedReviewCustomerDto = reviewCustomerService.saveReviewCustomer(reviewCustomerDto);
        } catch (CustomerServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ReviewCustomerDto>(updatedReviewCustomerDto, HttpStatus.OK);
    }

    @RequestMapping(value="/datatypes")
    public ResponseEntity<List> getDatatypes() {
        log.info("************************************* reviewcustomer-getDatatypes");

        List<DatatypeDto> datatypeDtoList = new ArrayList<>();

        DatatypeDto datatypeDto = new DatatypeDto();
        datatypeDto.setName("Address");
        datatypeDto.setDescription("Invalid Address");
        datatypeDtoList.add(datatypeDto);

        datatypeDto = new DatatypeDto();
        datatypeDto.setName("Email");
        datatypeDto.setDescription("Invalid Email");
        datatypeDtoList.add(datatypeDto);

        datatypeDto = new DatatypeDto();
        datatypeDto.setName("Zipcode");
        datatypeDto.setDescription("Invalid Zipcode");
        datatypeDtoList.add(datatypeDto);

        return new ResponseEntity<List>(datatypeDtoList, HttpStatus.OK);
    }

}
