package com.ws.customerservice.dto.giftcard;

import lombok.Data;

/**
 * ----------------------------------------------------------------------------
 * - Title:  Giftcard DTO
 * - Description:  This is the DTO for the Giftcard functionality
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto
 * - @date: 3/28/16
 * - @version $Rev$
 * -    3/28/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
public class GiftcardDto {

    public enum GC_REASON {
        NOT_IN_SYSTEM,
        ALREADY_ACTIVATED,
        NOT_ACCESSED,
        HAS_ACTIVITY
    }

    public enum GC_STATUS {
        SUCCESS,
        FAIL
    }

    private String number;
    private GC_STATUS status;
    private GC_REASON reason;

}
