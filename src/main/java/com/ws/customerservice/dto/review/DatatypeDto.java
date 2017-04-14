package com.ws.customerservice.dto.review;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.review
 * - @date: 8/8/16
 * - @version $Rev$
 * -    8/8/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class DatatypeDto {

    private String name;
    private String description;
    private String abbreviation;
}
