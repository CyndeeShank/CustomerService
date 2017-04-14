package com.ws.customerservice.model;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.model
 * - @date: 9/8/16
 * - @version $Rev$
 * -    9/8/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
public class DateDto {

    private Timestamp startDate;
    private Timestamp endDate;
}
