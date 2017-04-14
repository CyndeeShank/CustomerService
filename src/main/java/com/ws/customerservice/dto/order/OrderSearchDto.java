package com.ws.customerservice.dto.order;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ----------------------------------------------------------------------------
 * - Title:  OrderSearchDto class
 * - Description:  This class stores the Order Search Information
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.order
 * - @date: 9/7/16
 * - @version $Rev$
 * -    9/7/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class OrderSearchDto {

    public enum NUMBER_TYPE {
        WEB, INVOICE, ORDER, GIFTCARD, LOYALTY
    }
    public enum ADDRESS_TYPE {
        BILLING, SHIPPING
    }
    public enum MATCH_TYPE {
        EXACT, LIKE
    }
    private Long number;
    private String ccNo;
    private String lastName;
    private String firstName;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zip;

    private String searchType; // number, cc, name, email, address
    private String numberType = "web"; // web, invoice, order, giftcard, loyalty
    private String addressType = "billing"; // billing, shipping
    private String addressMatchType = "address"; // address, city, state, zip, all
    private String matchType = "exact"; // exact, like
    /**
    private NUMBER_TYPE numberType;
    private ADDRESS_TYPE addressType;
    private MATCH_TYPE matchType;
     **/
}
