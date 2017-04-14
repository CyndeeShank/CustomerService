package com.ws.customerservice.service;

import com.ws.customerservice.dao.LoyaltyDao;
import com.ws.customerservice.dto.loyalty.CustomerDto;
import com.ws.customerservice.dto.loyalty.LoyaltyDto;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.service
 * - @date: 6/21/16
 * - @version $Rev$
 * -    6/21/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Service
@Slf4j
public class LoyaltyService {

    @Autowired
    private LoyaltyDao loyaltyDao;

    public List<CustomerDto> searchLoyalty(LoyaltyDto loyaltyDto) {

        if (loyaltyDto.getEmail() != null) {
            loyaltyDto.setEmail(loyaltyDto.getEmail().toUpperCase());
        }
        if (loyaltyDto.getFirst() != null) {
            loyaltyDto.setFirst(loyaltyDto.getFirst().toUpperCase());
        }
        if (loyaltyDto.getLast() != null) {
            loyaltyDto.setLast(loyaltyDto.getLast().toUpperCase());
        }
        List<CustomerDto> loyaltyDtoList = loyaltyDao.daoLookupLoyaltyCustomers(loyaltyDto);

        /**
        //TEMP CODE for testing
        try {
            List<RmsInventoryDto> rmsInventoryDtoList = loyaltyDao.daoGetInventoryFromRms();
            //public List<Map<String, Object>> daoGetItemListFromRetekItemSetup() throws Exception {
            List<String> retekItemList = loyaltyDao.daoGetItemListFromRetekItemSetup();
            log.info("Size of RMS Inventory List: {}", rmsInventoryDtoList.size());
            log.info("Size of Retek Item List: {}", retekItemList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
         **/

        if (loyaltyDtoList == null) {
            // return no data found
        }
        return loyaltyDtoList;
    }

    public void deactivateLoyaltyCard(String number) throws CustomerServiceException {

        loyaltyDao.daoDeactivateLoyaltyCard(number);
    }

    public CustomerDto saveLoyaltyCustomer(CustomerDto customerDto) throws CustomerServiceException {

        try {
            // validate the fields...
            boolean newCustomer = true;

            if (customerDto.getCustomerId() != null) {
                // existing customer
                newCustomer = false;
            }
            else {
                // populate the customer id: 'S 00980 000111222333' (S + zero-padded store number + fic number)
                StringBuffer customerId = new StringBuffer();
                customerId.append("S");
                customerId.append(StringUtils.leftPad(customerDto.getSignUpStore().toString(), 5, "0"));
                customerId.append(customerDto.getLoyaltyNumber());
                customerDto.setCustomerId(customerId.toString());
            }

            // translate the timestamps to dates
            if (customerDto.getBdTimestamp() != null) {
                customerDto.setBirthDate(new java.sql.Date(customerDto.getBdTimestamp().getTime()));
            }
            else {
                // no date was entered, so default the birth date to today
                Calendar today = Calendar.getInstance();
                customerDto.setBirthDate(new java.sql.Date(today.getTimeInMillis()));
            }
            customerDto.setExpirationDate(new java.sql.Date(customerDto.getExpTimestamp().getTime()));

            // make sure the name(s) and email address is all uppercase
            customerDto.setFirstName(customerDto.getFirstName().toUpperCase());
            customerDto.setLastName(customerDto.getLastName().toUpperCase());
            if (customerDto.getEmailAddress() != null) {
                customerDto.setEmailAddress(customerDto.getEmailAddress().toUpperCase());
            }

            // save the Customer information
            loyaltyDao.daoSaveLoyaltyCustomer(customerDto, newCustomer);

        } catch (CustomerServiceException e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
        return customerDto;
    }
}


