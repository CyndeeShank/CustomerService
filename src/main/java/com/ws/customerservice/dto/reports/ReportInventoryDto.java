package com.ws.customerservice.dto.reports;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;


/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 9/29/16
 * - @version $Rev$
 * -    9/29/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class ReportInventoryDto {
    private String style;
    private String item;
    private String styleDesc;
    private String color;
    private int colorNo;
    private String size;
    private Timestamp clearanceDate;
    private String clearanceReason;
    private Timestamp lastReceivedDate;
    private Timestamp lastAllocatedDate;
    private int rmsInventoryQty;
    private int nfiOnHandQty;
    private int nfiOpenOrderQty;
    private String nfiInventoryType;
    private String imageUrl;

}
