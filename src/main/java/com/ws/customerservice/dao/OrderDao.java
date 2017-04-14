package com.ws.customerservice.dao;

import com.ws.customerservice.dto.order.*;
import com.ws.customerservice.model.StatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  OrderDao
 * - Description:  This class contains the Database calls
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dao
 * - @date: 5/20/16
 * - @version $Rev$
 * -    5/20/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Repository
public class OrderDao {

    // for testing update, we need to connect to DEV, not PROD
    //@Qualifier("getEMSJdbcTemplate")
    @Qualifier("getEMSProdJdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OrderDto> daoGetOrders(OrderDto.ORDER_STATUS orderStatus) {
        List<OrderDto> orderDtoList = new ArrayList<>();

        log.info("---=[ calling daoGetOrders with orderStatus: {} ]=---", orderStatus.getValue());

        // TODO: -- there are currently 17000+ "New Orders" in dev, but only 1114 in prod --
        // date created sort is temporary
        String sql = "SELECT top 200 order_no, web_order_no, billto_first_name, billto_last_name, shipto_first_name, " +
                "shipto_last_name, ship_method, date_created, current_status, current_status_memo, total_amt, web_store " +
                "FROM ord_order where current_status = '" + orderStatus.getValue() + "' " +
                "ORDER by date_created DESC";
        log.info("-=[ Executing SQL: {} ]=-", sql);
        orderDtoList = jdbcTemplate.query(sql, new OrderDtoMapper());

        return orderDtoList;
    }

    /**
     * This method is for testing the update, so only return a list of my (cyndee's) orders
     * @return
     */
    public List<OrderDto> daoGetOrdersTest() {
        List<OrderDto> orderDtoList = new ArrayList<>();

        log.info("---=[ calling daoGetOrdersTest  ]=---");

        // date created sort is temporary
        String sql = "SELECT top 200 order_no, web_order_no, billto_first_name, billto_last_name, shipto_first_name, " +
                "shipto_last_name, ship_method, date_created, current_status, current_status_memo, total_amt, web_store " +
                "FROM ord_order where billto_last_name like 'Shank' and billto_first_name like 'Cyndee' " +
                "ORDER by date_created DESC";
        log.info("-=[ Executing SQL: {} ]=-", sql);
        orderDtoList = jdbcTemplate.query(sql, new OrderDtoMapper());

        return orderDtoList;
    }

    protected static final class OrderDtoMapper implements RowMapper<OrderDto> {
        public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderDto orderDto = new OrderDto();

            orderDto.setOrderNo(rs.getLong("order_no"));
            orderDto.setWebOrderNo(rs.getLong("web_order_no"));
            orderDto.setBillingFirstName(rs.getString("billto_first_name"));
            orderDto.setBillingLastName(rs.getString("billto_last_name"));
            orderDto.setShippingFirstName(rs.getString("shipto_first_name"));
            orderDto.setShippingLastName(rs.getString("shipto_last_name"));
            String shippingMethod = rs.getString("ship_method");
            orderDto.setShipMethod(getShippingMethod(shippingMethod));
            orderDto.setDateOrdered(rs.getDate("date_created"));
            orderDto.setCurrentStatus(rs.getString("current_status"));
            orderDto.setCurrentStatusMemo(rs.getString("current_status_memo"));
            orderDto.setTotalAmount(rs.getBigDecimal("total_amt"));
            int webStore = rs.getInt("web_store");
            orderDto.setOrderType(getOrderType(webStore));

            return orderDto;
        }
    }

    /**
     * search for matching order(s) by loyalty number or gift card number
     */
    public List<OrderDto> daoSearchOrderByCard(String matchType, String number) {

        List<OrderDto> orderDtoList = new ArrayList<>();

        try {
            orderDtoList = jdbcTemplate.execute(" { call app_search_order_by_card(?,?)}",
                    (CallableStatementCallback<List<OrderDto>>) callableStatement -> {
                        callableStatement.setString("number", number);
                        callableStatement.setString("matchType", matchType);

                        ResultSet rs = callableStatement.executeQuery();

                        List<OrderDto> orderDtoList1 = new ArrayList<>();
                        while (rs.next())
                        {
                            OrderDto orderDto = populateOrderDto(rs);
                            orderDtoList1.add(orderDto);
                        }
                        return orderDtoList1;
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderDtoList;
    }

    public List<OrderDto> daoSearchOrders(OrderSearchDto orderSearchDto) {

        List<OrderDto> orderDtoList = new ArrayList<>();

        try {
            orderDtoList = jdbcTemplate.execute(" { call app_search_orders(?,?,?,?,?,?,?,?,?,?,?,?,?)}",
                    (CallableStatementCallback<List<OrderDto>>) callableStatement -> {
                        callableStatement.setLong("number", orderSearchDto.getNumber());
                        callableStatement.setString("ccNo", orderSearchDto.getCcNo());
                        callableStatement.setString("lastName", orderSearchDto.getLastName());
                        callableStatement.setString("firstName", orderSearchDto.getFirstName());
                        callableStatement.setString("email", orderSearchDto.getEmail());
                        callableStatement.setString("address", orderSearchDto.getAddress());
                        callableStatement.setString("city", orderSearchDto.getCity());
                        callableStatement.setString("state", orderSearchDto.getState());
                        callableStatement.setString("zip", orderSearchDto.getZip());
                        callableStatement.setString("searchType", orderSearchDto.getSearchType());
                        callableStatement.setString("numberType", orderSearchDto.getNumberType());
                        callableStatement.setString("addressType", orderSearchDto.getAddressType());
                        callableStatement.setString("matchType", orderSearchDto.getAddressMatchType());

                        ResultSet rs = callableStatement.executeQuery();

                        List<OrderDto> orderDtoList1 = new ArrayList<>();
                        while (rs.next())
                        {
                            OrderDto orderDto = populateOrderDto(rs);
                            orderDtoList1.add(orderDto);
                        }
                        return orderDtoList1;
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderDtoList;
    }


    public OrderDetailDto daoGetOrderDetailsById(Long orderId, OrderDetailDto orderDetailDto) {
        OrderDetailDto updatedOrderDetailDto = orderDetailDto;
        log.info("-=[ calling daoGetOrderDetailsById with orderId: {} ]=-", orderId);
        try {
            orderDetailDto = jdbcTemplate.execute(" { call app_get_order_details(?)}",
                    (CallableStatementCallback<OrderDetailDto>) callableStatement -> {
                        callableStatement.setLong(1, orderId);
                        ResultSet rs = callableStatement.executeQuery();
                        OrderDetailDto orderDetailDto1 = updatedOrderDetailDto;
                        while (rs.next())
                        {
                            orderDetailDto1.setLoyaltyNo(rs.getString("frequent_buyer_card_no"));
                            // set billing fields
                            orderDetailDto1.setBillingMiddleI(rs.getString("billto_middle_int"));
                            orderDetailDto1.setBillingEmail(rs.getString("billto_email_address"));
                            orderDetailDto1.setBillingAddress(rs.getString("billto_address"));
                            orderDetailDto1.setBillingAddress2(rs.getString("billto_address2"));
                            orderDetailDto1.setBillingCity(rs.getString("billto_city"));
                            orderDetailDto1.setBillingState(rs.getString("billto_state"));
                            orderDetailDto1.setBillingZip(rs.getString("billto_zip"));
                            orderDetailDto1.setBillingCountry(rs.getString("billto_country"));
                            orderDetailDto1.setBillingPhone(rs.getString("billto_phone"));
                            orderDetailDto1.setBillingCellphone(rs.getString("billto_cellphone"));

                            // set shipping fields
                            orderDetailDto1.setShippingMiddleI(rs.getString("shipto_middle_int"));
                            orderDetailDto1.setShippingEmail(rs.getString("shipto_email_address"));
                            orderDetailDto1.setShippingAddress(rs.getString("shipto_address"));
                            orderDetailDto1.setShippingAddress2(rs.getString("shipto_address2"));
                            orderDetailDto1.setShippingCity(rs.getString("shipto_city"));
                            orderDetailDto1.setShippingState(rs.getString("shipto_state"));
                            orderDetailDto1.setShippingZip(rs.getString("shipto_zip"));
                            orderDetailDto1.setShippingCountry(rs.getString("shipto_country"));
                            orderDetailDto1.setShippingPhone(rs.getString("shipto_phone"));

                            orderDetailDto1.setTrackingNo(rs.getString("ship_tracking_no"));
                            orderDetailDto1.setShippingComplete(rs.getBoolean("ship_complete"));
                            orderDetailDto1.setShippingCarrier(rs.getString("ship_carrier"));
                            orderDetailDto1.setDateShipped(rs.getTimestamp("date_shipped"));

                            // set amount/money fields
                            orderDetailDto1.setShipAmount(rs.getBigDecimal("ship_amount"));
                            orderDetailDto1.setDiscountShip(rs.getBigDecimal("discount_ship"));
                            orderDetailDto1.setMerchandiseTotal(rs.getBigDecimal("merchandise_total"));
                            orderDetailDto1.setTaxRate(rs.getBigDecimal("tax_rate"));
                            orderDetailDto1.setTaxTotal(rs.getBigDecimal("tax_total"));
                            orderDetailDto1.setDiscountTotal(rs.getBigDecimal("discount_total"));
                            orderDetailDto1.setGcTotal(rs.getBigDecimal("giftCard_Total"));
                            orderDetailDto1.setShipTax(rs.getBigDecimal("ship_tax"));

                            orderDetailDto1.setTotalItems(rs.getInt("total_items"));
                            orderDetailDto1.setDiscountList(rs.getString("discount_list"));

                            // order cancelled fields
                            orderDetailDto1.setOrderCancelled(rs.getBoolean("order_cancelled"));
                            orderDetailDto1.setQtyCancelled(rs.getInt("qty_cancelled"));
                            orderDetailDto1.setCancellationDate(rs.getTimestamp("cancelled_date"));
                            orderDetailDto1.setCancellationReason(rs.getString("cancellation_reason"));

                            // order on hold fields
                            orderDetailDto1.setOrderOnHold(rs.getBoolean("order_on_hold"));
                            orderDetailDto1.setHoldDate(rs.getTimestamp("hold_date"));

                            // order fulfilled fields
                            orderDetailDto1.setQtyShipped(rs.getInt("qty_shipped"));
                            orderDetailDto1.setOrderFulfilled(rs.getBoolean("order_fullfilled"));
                            orderDetailDto1.setOrderFulfillmentDate(rs.getTimestamp("order_fullfillment_date"));

                            // loyalty number and ref code (Amazon Order Id)
                            orderDetailDto1.setLoyaltyNo(rs.getString("frequent_buyer_card_no"));
                            orderDetailDto1.setAmazonOrderNo(rs.getString("ref_code"));
                        }
                        return orderDetailDto1;
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderDetailDto;
    }

    public List<OrderLineItemDto> daoGetOrderLineItemsById(Long orderId) {

        List<OrderLineItemDto> orderLineItemDtoList = new ArrayList<>();

        try {
            orderLineItemDtoList = jdbcTemplate.execute(" { call app_get_order_lineitems(?)}",
                    (CallableStatementCallback<List<OrderLineItemDto>>) callableStatement -> {
                        callableStatement.setLong(1, orderId);
                        ResultSet rs = callableStatement.executeQuery();
                        List<OrderLineItemDto> orderLineItemDtoList1 = new ArrayList<OrderLineItemDto>();
                        while (rs.next())
                        {
                            OrderLineItemDto orderLineItemDto1 = new OrderLineItemDto();
                            orderLineItemDto1.setOrderNo(orderId);

                            // set item type fields
                            orderLineItemDto1.setSku(rs.getString("sku"));
                            orderLineItemDto1.setStyle(rs.getString("style_no"));
                            orderLineItemDto1.setStyleDesc(rs.getString("style_desc"));
                            orderLineItemDto1.setSize(rs.getString("size_desc"));
                            orderLineItemDto1.setColorNumber(rs.getInt("color_no"));
                            orderLineItemDto1.setColorDesc(rs.getString("color_desc"));
                            orderLineItemDto1.setQuantity(rs.getInt("qty"));
                            String tmp = rs.getString("item_code");
                            if (tmp != null) {
                                orderLineItemDto1.setAmazonItemCode(tmp);
                            }
                            orderLineItemDto1.setInStock(rs.getBoolean("in_stock"));

                            // set price, discount and tax values
                            orderLineItemDto1.setUnitPrice(rs.getBigDecimal("unit_price"));
                            orderLineItemDto1.setDiscountAmountEach(rs.getBigDecimal("discount_amount_each"));
                            orderLineItemDto1.setDiscountPercentEach(rs.getBigDecimal("discount_percent_each"));
                            orderLineItemDto1.setDiscountFIC(rs.getBigDecimal("discount_fbc"));
                            orderLineItemDto1.setDiscountable(rs.getBoolean("discountable"));
                            orderLineItemDto1.setTaxable(rs.getBoolean("taxable"));
                            orderLineItemDto1.setItemDiscountList(rs.getString("item_discount_list"));

                            orderLineItemDto1.setUnitTax(rs.getBigDecimal("unit_tax"));
                            orderLineItemDto1.setItemTaxRate(rs.getBigDecimal("item_tax_rate"));

                            // set date values
                            orderLineItemDto1.setDateCreated(rs.getTimestamp("date_created"));
                            Date tmpDate = rs.getTimestamp("date_canceled");
                            if (tmpDate != null) {
                                orderLineItemDto1.setDateCancelled(tmpDate);
                            }
                            tmpDate = rs.getTimestamp("date_shipped");
                            if (tmpDate != null) {
                                orderLineItemDto1.setDateShipped(tmpDate);
                            }
                            tmpDate = rs.getTimestamp("date_returned");
                            if (tmpDate != null) {
                                orderLineItemDto1.setDateReturned(tmpDate);
                            }

                            orderLineItemDto1.setShipQty(rs.getInt("ship_qty"));

                            // cancelled item values
                            tmp = rs.getString("cancel_memo");
                            if (tmp != null) {
                                orderLineItemDto1.setCancelMemo(tmp);
                            }
                            orderLineItemDto1.setCancelQty(rs.getInt("cancel_qty"));

                            Boolean tmpBoolean = rs.getBoolean("cancellation_email_sent");
                            if (tmpBoolean != null) {
                                orderLineItemDto1.setCancellationEmailSent(tmpBoolean);
                            }

                            // return/refund item values
                            tmp = rs.getString("return_memo");
                            if (tmp != null) {
                                orderLineItemDto1.setReturnMemo(tmp);
                            }
                            orderLineItemDto1.setReturnQty(rs.getInt("return_qty"));

                            tmp = rs.getString("return_reason_code");
                            if (tmp != null) {
                                orderLineItemDto1.setReturnReasonCode(tmp);
                            }
                            orderLineItemDto1.setRefundQty(rs.getInt("refund_qty"));

                            Integer tmpInt = rs.getInt("refund_status");
                            if (tmpInt != null) {
                                orderLineItemDto1.setRefundStatus(tmpInt);
                            }
                            orderLineItemDto1.setCurrentStatus(rs.getString("current_status"));

                            tmp = rs.getString("current_status_memo");
                            if (tmp != null) {
                                orderLineItemDto1.setCurrentStatusMemo(tmp);
                            }
                            Long tmpLong = rs.getLong("value_card_no");
                            if (tmpLong != null) {
                                orderLineItemDto1.setFicCardNumber(tmpLong);
                            }
                            orderLineItemDtoList1.add(orderLineItemDto1);
                        }
                        return orderLineItemDtoList1;
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderLineItemDtoList;
    }

    public List<OrderStatusDto> daoGetOrderStatusById(Long orderId) {

        List<OrderStatusDto> orderStatusDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetOrderStatusById with orderId: {} ]=-", orderId);

        try {
            orderStatusDtoList = jdbcTemplate.execute(" { call app_get_order_status(?)}",
            //orderStatusDtoList = jdbcTemplate.execute(" { call sproc_get_order_status_chronologically(?,?)}",
                    (CallableStatementCallback<List<OrderStatusDto>>) callableStatement -> {
                        callableStatement.setLong(1, orderId);
                        //callableStatement.setString(2, "transaction");
                        ResultSet rs = callableStatement.executeQuery();
                        List<OrderStatusDto> orderStatusDtoList1 = new ArrayList<>();
                        while (rs.next())
                        {
                            OrderStatusDto orderStatusDto = new OrderStatusDto();
                            orderStatusDto.setOrderNo(orderId);
                            orderStatusDto.setTimestamp(rs.getTimestamp("time_stamp"));
                            orderStatusDto.setAgentName(rs.getString("agent_id"));
                            orderStatusDto.setStatus(rs.getString("status"));
                            /**
                             orderStatusDto.setAgentName(rs.getString("agent_name"));
                            String itemType = rs.getString("lineitem_type");
                            if (itemType.equalsIgnoreCase("O")) {
                               orderStatusDto.setItemType(OrderStatusDto.ITEM_TYPE.ORDER);
                            }
                            else if (itemType.equalsIgnoreCase("OLI")) {
                                orderStatusDto.setItemType(OrderStatusDto.ITEM_TYPE.LINEITEM);
                            }
                            else if (itemType.equalsIgnoreCase("T")) {
                                orderStatusDto.setItemType(OrderStatusDto.ITEM_TYPE.TRANSACTION);
                            }
                             **/

                            orderStatusDtoList1.add(orderStatusDto);
                        }
                        return orderStatusDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return orderStatusDtoList;
    }

    public List<OrderPaymentDto> daoGetOrderPaymentById(Long orderId) {

        List<OrderPaymentDto> orderPaymentDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetOrderPaymentById with orderId: {} ]=-", orderId);

        try {
            orderPaymentDtoList = jdbcTemplate.execute(" { call app_get_order_payment(?)}",
                    (CallableStatementCallback<List<OrderPaymentDto>>) callableStatement -> {
                        callableStatement.setLong(1, orderId);
                        ResultSet rs = callableStatement.executeQuery();
                        List<OrderPaymentDto> orderPaymentDtoList1 = new ArrayList<>();
                        while (rs.next())
                        {
                            OrderPaymentDto orderPaymentDto = new OrderPaymentDto();
                            orderPaymentDto.setOrderNo(orderId);
                            orderPaymentDto.setPaymentType(rs.getString("payment_type"));
                            orderPaymentDto.setCardName(rs.getString("nameoncard"));
                            orderPaymentDto.setExpirationDate(rs.getString("expiration_date"));
                            orderPaymentDto.setPreauthAmount(rs.getBigDecimal("preauth_amt"));
                            orderPaymentDto.setPaymentAmount(rs.getBigDecimal("payment_amt"));
                            orderPaymentDto.setAuthCode(rs.getString("auth_code"));
                            orderPaymentDto.setAvsCode(rs.getString("avs_code"));
                            orderPaymentDto.setCcHint(rs.getString("ccHint"));
                            orderPaymentDtoList1.add(orderPaymentDto);
                        }
                        return orderPaymentDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return orderPaymentDtoList;
    }

    public StatusDto daoResubmitOrder(Long orderId) {

        StatusDto statusDto = new StatusDto();

        log.info("-=[ calling daoResubmitOrder with orderId: {} ]=-", orderId);

        try {
            statusDto = jdbcTemplate.execute(" { call resend_order_to_distribution(?)}",
                    (CallableStatementCallback<StatusDto>) callableStatement -> {
                        callableStatement.setLong(1, orderId);
                        ResultSet rs = callableStatement.executeQuery();
                        StatusDto statusDto1 = new StatusDto();
                        while (rs.next())
                        {
                            statusDto1.setResponseCode(rs.getInt("response_code"));
                            statusDto1.setResponseMessage(rs.getString("response_message"));
                        }
                        return statusDto1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return statusDto;
    }

    public Long daoGetOrderNoFromInvoiceNo(String invoiceNo) {

        Long orderNumber = null;

        try {
            String sql = "select order_no from ord_invoice where invoice_no = " + invoiceNo;

            RowMapper<Long> mapper = new RowMapper<Long>() {
                public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Long orderNo = rs.getLong(("order_no"));
                    return orderNo;
                }
            };
            log.info("SQL: {}", sql);
            orderNumber = jdbcTemplate.queryForObject(sql, mapper);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderNumber;
    }

    private static OrderDto.SHIP_METHOD getShippingMethod(String shippingMethod) {

        OrderDto.SHIP_METHOD method = null;

        if (shippingMethod.startsWith("Ground")) {
            method = OrderDto.SHIP_METHOD.GROUND;
        } else if (shippingMethod.startsWith("Second")) {
            method = OrderDto.SHIP_METHOD.SECOND;
        } else if (shippingMethod.startsWith("Next")) {
            method = OrderDto.SHIP_METHOD.NEXT;
        }

        return method;
    }

    private static OrderDto.ORDER_TYPE getOrderType(int webStore) {

        OrderDto.ORDER_TYPE orderType = null;

        switch (webStore) {
            case 8:
                orderType = OrderDto.ORDER_TYPE.AMZ;
                break;
            case 12:
                orderType = OrderDto.ORDER_TYPE.DW;
                break;
            case 5:
                orderType = OrderDto.ORDER_TYPE.CREDIT;
                break;
            default:
                orderType = OrderDto.ORDER_TYPE.OTHER;
                break;
        }
        return orderType;
    }

    private OrderDto populateOrderDto(ResultSet rs) throws SQLException {

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderNo(rs.getLong("order_no"));
        orderDto.setWebOrderNo(rs.getLong("web_order_no"));
        orderDto.setBillingFirstName(rs.getString("billto_first_name"));
        orderDto.setBillingLastName(rs.getString("billto_last_name"));
        orderDto.setShippingFirstName(rs.getString("shipto_first_name"));
        orderDto.setShippingLastName(rs.getString("shipto_last_name"));
        String shippingMethod = rs.getString("ship_method");
        orderDto.setShipMethod(getShippingMethod(shippingMethod));
        orderDto.setDateOrdered(rs.getDate("date_created"));
        orderDto.setCurrentStatus(rs.getString("current_status"));
        orderDto.setCurrentStatusMemo(rs.getString("current_status_memo"));
        orderDto.setTotalAmount(rs.getBigDecimal("total_amt"));
        int webStore = rs.getInt("web_store");
        orderDto.setOrderType(getOrderType(webStore));

        return orderDto;
    }
}

