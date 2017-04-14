package com.ws.customerservice.dto.order;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.order
 * - @date: 5/20/16
 * - @version $Rev$
 * -    5/20/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class OrderDto {

    public enum SHIP_METHOD {
        NEXT, SECOND, GROUND, STANDARD
    }
    public enum ORDER_TYPE {
        DW, AMZ, CREDIT, OTHER
    }

    public enum ORDER_STATUS {
        NEW ("NEW ORDER"),
        SENT_TO_DIST ("SENT TO DISTRIBUTION"),
        SHIPPED ("ORDER SHIPPED"),
        AUTO_HOLD ("AUTO ON HOLD"),
        MANUAL_HOLD ("MANUAL ON HOLD"),
        RELEASED_HOLD ("RELEASED FROM HOLD"),
        CANCELLED ("ORDER CANCELLED");

        private String value;
        ORDER_STATUS(String value) {
            this.value = value;
        }
        public String getValue() {
            return this.value;
        }
    }

    private Long orderNo;
    private Long webOrderNo;
    private String amazonOrderNo;
    private String billingFirstName;
    private String billingMiddleI;
    private String billingLastName;
    private String shippingFirstName;
    private String shippingMiddleI;
    private String shippingLastName;
    private BigDecimal totalAmount;
    private SHIP_METHOD shipMethod;
    private ORDER_TYPE orderType;
    private Date dateOrdered;
    private Date dateShipped;
    private String currentStatus;
    private String currentStatusMemo;

    private Long invoiceNo;
}
