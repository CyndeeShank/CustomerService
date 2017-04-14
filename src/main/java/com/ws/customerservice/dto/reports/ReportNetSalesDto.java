package com.ws.customerservice.dto.reports;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 6/14/16
 * - @version $Rev$
 * -    6/14/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
public class ReportNetSalesDto {

    private Date startDate;
    private Date endDate;
    private BigDecimal salesValue;
    private int salesNumber;
    private BigDecimal giftCardValue;
    private int giftCardNumber;
    private BigDecimal returnsValue;
    private int returnsNumber;
    private BigDecimal loyaltyValue;
    private int loyaltyNumber;
    private int salesUnits;
    private int returnUnits;
    private BigDecimal allowanceValue;
    private int allowanceNumber;
    private BigDecimal paypalValue;
    private int paypalNumber;
    private BigDecimal cbaAmazonValue;
    private int cbaAmazonNumber;
}
