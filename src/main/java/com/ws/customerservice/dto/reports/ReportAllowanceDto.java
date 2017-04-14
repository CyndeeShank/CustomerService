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
public class ReportAllowanceDto {

    private Long transactionId;
    private Date transactionDate;
    private Long orderNumber;
    private String description;
    private BigDecimal transactionAmount;
    private String comments;
}
