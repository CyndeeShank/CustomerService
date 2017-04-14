package com.ws.customerservice.dto.reports;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 9/16/16
 * - @version $Rev$
 * -    9/16/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class ReportTransactionDto {
    private Long id;
    private Timestamp date;
    private Long orderNo;
    private String type;
    private String status;
    private BigDecimal amount;
    private String accountNum;
}
