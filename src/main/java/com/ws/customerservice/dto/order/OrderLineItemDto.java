package com.ws.customerservice.dto.order;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title: OrderLineItemDTO
 * - Description:  This class stores the Order Line Item information
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.order
 * - @date: 6/1/16
 * - @version $Rev$
 * -    6/1/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class OrderLineItemDto {

    private Long orderNo;
    private String sku;
    private String style;
    private String styleDesc;
    private String size;
    private int colorNumber;
    private String colorDesc;
    private BigDecimal unitPrice;
    private int quantity;
    private Date dateCreated;
    private Boolean discountable;
    private Boolean taxable;
    private BigDecimal discountAmountEach;
    private BigDecimal discountPercentEach;
    private BigDecimal discountFIC;
    private Date dateCancelled;
    private String cancelMemo;
    private int cancelQty;
    private Boolean cancellationEmailSent;
    private Date dateShipped;
    private int shipQty;
    private Date dateReturned;
    private String returnMemo;
    private int returnQty;
    private int refundQty;
    private boolean inStock;
    private String currentStatus;
    private String currentStatusMemo;
    private String returnReasonCode;
    private String itemDiscountList;
    private Long ficCardNumber;
    private int refundStatus;
    private String amazonItemCode;
    private BigDecimal unitTax;
    private BigDecimal itemTaxRate;
}
