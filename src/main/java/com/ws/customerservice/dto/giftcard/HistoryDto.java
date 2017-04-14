package com.ws.customerservice.dto.giftcard;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.giftcard
 * - @date: 8/24/16
 * - @version $Rev$
 * -    8/24/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
@Slf4j
public class HistoryDto {

    private String date;
    private String time;
    private String amount;
    private String sign;
    private String balance;
}
