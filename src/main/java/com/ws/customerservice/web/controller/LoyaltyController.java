package com.ws.customerservice.web.controller;

import com.ws.customerservice.dto.loyalty.CustomerDto;
import com.ws.customerservice.dto.loyalty.LoyaltyDto;
import com.ws.customerservice.model.CustomerServiceException;
import com.ws.customerservice.service.LoyaltyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 6/23/16
 * - @version $Rev$
 * -    6/23/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Controller
@RequestMapping("/loyalty")
public class LoyaltyController {

    @Autowired
    private LoyaltyService loyaltyService;

    private List<CustomerDto> currentCustomerDtoList;
    private CustomerDto currentCustomerDto;


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<List<CustomerDto>> searchLoyalty(@RequestBody LoyaltyDto loyaltyDto) {
        log.info("************************************* search");

        List<CustomerDto> customerDtoList = loyaltyService.searchLoyalty(loyaltyDto);
        // if a single match is found, then save it locally for the detail lookup, otherwise save the list for
        // later lookup by customerId, instead of going back to the database
        // TODO:  may need to move this to a static class
        if (customerDtoList.size() == 1) {
            currentCustomerDto = customerDtoList.get(0);
        } else {
            currentCustomerDtoList = customerDtoList;
        }
        if (customerDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customerDtoList, HttpStatus.OK);
    }


    @RequestMapping(value = "/detail/{number}", method = RequestMethod.GET)
    public ResponseEntity<CustomerDto> getDetailByNumber(@PathVariable String number) {
        log.info("************************************* getDetailByNumber");

        // loop thru the list and find the entry with the matching loyalty number
        for (CustomerDto customerDto : currentCustomerDtoList) {
            if (customerDto.getLoyaltyNumber().equals(number)) {
                currentCustomerDto = customerDto;
            }
        }
        if (currentCustomerDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(currentCustomerDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseEntity<CustomerDto> getDetail() {
        log.info("************************************* getDetail");

        if (currentCustomerDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(currentCustomerDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<CustomerDto> saveLoyaltyCustomer(@RequestBody @Valid CustomerDto customerDto) {
        log.info("************************************* saveLoyaltyCustomer");
        CustomerDto updatedCustomerDto = null;
        try {
            updatedCustomerDto = loyaltyService.saveLoyaltyCustomer(customerDto);
        } catch (CustomerServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<CustomerDto>(updatedCustomerDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    public ResponseEntity<LoyaltyDto> deactivateLoyaltyCard(@RequestParam String number) {
        log.info("************************************* deactivateLoyaltyCard");
        LoyaltyDto loyaltyDto = new LoyaltyDto();
        try {
            loyaltyService.deactivateLoyaltyCard(number);
            loyaltyDto.setStatus("success");
        } catch (CustomerServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(loyaltyDto, HttpStatus.OK);

    }

}

