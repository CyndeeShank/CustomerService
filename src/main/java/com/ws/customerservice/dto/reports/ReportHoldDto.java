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
@Data
@Slf4j
public class ReportHoldDto {

    private long emsOrderNo;
    private long webOrderNo;
    private BigDecimal merchTotal;
    private Timestamp dateOnHold;
    private Timestamp dateOffHold;
    private String reasonOnHold;
    private String reasonOffHold;
    private Boolean manualHold;
}
