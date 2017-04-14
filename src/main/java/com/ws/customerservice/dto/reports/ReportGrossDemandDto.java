package com.ws.customerservice.dto.reports;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 3/30/16
 * - @version $Rev$
 * -    3/30/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
public class ReportGrossDemandDto {

    private Date orderDate;
    private int numOrders;
    private BigDecimal merchandiseTotal;
    private BigDecimal adsTotal;
    private BigDecimal shippingTotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal otherChargeTotal;
    private int numUnits;
}
