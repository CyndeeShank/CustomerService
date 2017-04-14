package com.ws.customerservice.dto.order;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

/**
 * ----------------------------------------------------------------------------
 * - Title:  OrderStatusDto
 * - Description:  This class stores the Order Status Information fields
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.order
 * - @date: 6/6/16
 * - @version $Rev$
 * -    6/6/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class OrderStatusDto {

    public enum ITEM_TYPE {
        ORDER,
        LINEITEM,
        TRANSACTION
    }
    private Long orderNo;
    private Timestamp timestamp;
    private String agentName;
    private ITEM_TYPE itemType;
    private String status;
}
