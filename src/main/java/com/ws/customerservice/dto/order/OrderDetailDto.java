package com.ws.customerservice.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class OrderDetailDto extends OrderDto {

    private BigDecimal discountShip;
    private BigDecimal discountTotal;
    private BigDecimal merchandiseTotal;
    private BigDecimal shipAmount;
    private BigDecimal shipTax;
    private BigDecimal taxRate;
    private BigDecimal taxTotal;
    private BigDecimal gcTotal;

    private int totalItems;

    private boolean gift;
    private String giftMessage;

    private String billingAddress;
    private String billingAddress2;
    private String billingCellphone;
    private String billingCity;
    private String billingCountry;
    private String billingEmail;
    private String billingPhone;
    private String billingState;
    private String billingZip;

    private String discountList;
    private String loyaltyNo;

    private String shippingAddress;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingCountry;
    private String shippingEmail;
    private String shippingPhone;
    private String shippingState;
    private String shippingZip;
    private String trackingNo;
    private boolean shippingComplete;
    private String shippingCarrier;

    private boolean orderCancelled;
    private int qtyCancelled;
    private Date cancellationDate;
    private String cancellationReason;

    private int qtyShipped;
    private boolean orderOnHold;
    private Date holdDate;

    private boolean orderFulfilled;
    private Date orderFulfillmentDate;

    private String refCode;
    private String reportId;

    private List<OrderLineItemDto> orderLineItemDtoList;
}
