package com.ws.customerservice.dto.reports;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2017
 * - Company:  Wet Seal; LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 1/5/17
 * - @version $Rev$
 * -    1/5/17 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class ReportShippingDto {

    private long emsOrderNo;
    private String shippingFirstName;
    private String shippingMiddleI;
    private String shippingLastName;
    private String shippingAddress;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;
    private BigDecimal shippingAmount;
    private BigDecimal discountShip;
    private String shipTrackingNo;
    private String shipCarrier;
    private Timestamp dateShipped;
}
