package com.ws.customerservice.dto.reports;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 9/13/16
 * - @version $Rev$
 * -    9/13/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class ReportOrderBatchDto {
    private int batchId;
    private Date dateCreated;
    private int numOrders;
    private int pendingOrders;
    private Date dateCompleted;
    private Boolean nfiFileGenerated;
    private String nfiFilename;
    private Date dateSentToNfi;
    private ShipMethodDto shipMethod;

}
