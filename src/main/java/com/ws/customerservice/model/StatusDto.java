package com.ws.customerservice.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.model
 * - @date: 12/8/16
 * - @version $Rev$
 * -    12/8/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class StatusDto {

    private int responseCode;
    private String responseMessage;
}
