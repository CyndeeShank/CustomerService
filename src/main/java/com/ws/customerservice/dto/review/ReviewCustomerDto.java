package com.ws.customerservice.dto.review;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.review
 * - @date: 8/4/16
 * - @version $Rev$
 * -    8/4/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
public class ReviewCustomerDto {
    public enum FRAUD_TYPE {
        EMAIL, ADDRESS, ZIPCODE
    }
    private Integer id;
    private String matchValue;
    private FRAUD_TYPE fraudType;
    private String address;
    private String zip;
    private String email;
    private Date dateCreated;
    private String reason;
    private int status;
}
