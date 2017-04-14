package com.ws.customerservice.dto.reports;

import lombok.Data;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 9/8/16
 * - @version $Rev$
 * -    9/8/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
public class ReportReturnsDto {

    private String style;
    private String color;
    private int quantity;
    private String description;
    private int reasonCode;
    private String reasonDesc;
}
