package com.ws.customerservice.dto.loyalty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.loyalty
 * - @date: 6/21/16
 * - @version $Rev$
 * -    6/21/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class LoyaltyDto {

    public enum SEARCH_TYPE {
        NAME, NUMBER, EMAIL, ALL
    }

    //private String customerId;
    private String last;
    private String first;
    private String email;
    private String number;
    private String searchType;
    private String status;
    //private SEARCH_TYPE searchType;

    /**
    private String status;
    private boolean valid;
    private String address;
    private String city;
    private String state;
    private String phone;
    private Timestamp expTimestamp;
    private boolean cardExpired;
    private boolean emailMatch;
    private boolean cardValid;
     **/
}
