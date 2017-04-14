package com.ws.customerservice.dto.order;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.awt.image.PixelConverter;

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
 * - @date: 6/7/16
 * - @version $Rev$
 * -    6/7/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class OrderPaymentDto {

    private Long orderNo;
    private String paymentType;
    private String cardName;
    private String expirationDate;
    private BigDecimal preauthAmount;
    private BigDecimal paymentAmount;
    private String authCode;
    private String avsCode;
    private String ccHint;
}
