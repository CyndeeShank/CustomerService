package com.ws.customerservice.web.controller;

import com.ws.customerservice.dto.order.*;
import com.ws.customerservice.model.StatusDto;
import com.ws.customerservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  OrderController
 * - Description:  This is the controller class for handling all order related
 *                  requests for the Customer Service Application
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 5/20/16
 * - @version $Rev$
 * -    5/20/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value="/new")
    public ResponseEntity<List<OrderDto>> getNewOrders() {
        log.info("************************************* orders-new");

        List<OrderDto> orderDtoList = orderService.getNewOrders();

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }
    @RequestMapping(value="/dist")
    public ResponseEntity<List<OrderDto>> getDistributionOrders() {
        log.info("************************************* orders-dist");

        List<OrderDto> orderDtoList = orderService.getDistributionOrders();

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }
    @RequestMapping(value="/ship")
    public ResponseEntity<List<OrderDto>> getShippedOrders() {
        log.info("************************************* orders-shipped");

        List<OrderDto> orderDtoList = orderService.getShippedOrders();

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/auto")
    public ResponseEntity<List<OrderDto>> getAutoHoldOrders() {
        log.info("************************************* orders-auto-hold");

        List<OrderDto> orderDtoList = orderService.getAutoHoldOrders();

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/manual")
    public ResponseEntity<List<OrderDto>> getManualHoldOrders() {
        log.info("************************************* orders-manual-hold");

        List<OrderDto> orderDtoList = orderService.getManualHoldOrders();

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/cancel")
    public ResponseEntity<List<OrderDto>> getCancelledOrders() {
        log.info("************************************* orders-cancelled");

        List<OrderDto> orderDtoList = orderService.getCancelledOrders();

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<List<OrderDto>> searchOrders(@RequestBody OrderSearchDto orderSearchDto) {
        log.info("************************************* search-Orders");

        List<OrderDto> orderDtoList = orderService.searchOrders(orderSearchDto);

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/{id}")
    public ResponseEntity<OrderDto> getOrderDetailsById(@PathVariable Long id) {
        log.info("************************************* order-by-id");

        OrderDetailDto orderDetailDto = orderService.getOrderDetailsById(id);

        if (orderDetailDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDetailDto, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/status/{id}")
    public ResponseEntity<List<OrderStatusDto>> getOrderStatusById(@PathVariable Long id) {
        log.info("************************************* order-status-by-id");

        List<OrderStatusDto> orderStatusDtoList = orderService.getOrderStatusById(id);

        if (orderStatusDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderStatusDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/payment/{id}")
    public ResponseEntity<List<OrderPaymentDto>> getOrderPaymentById(@PathVariable Long id) {
        log.info("************************************* order-Payment-by-id");

        List<OrderPaymentDto> orderPaymentDtoList = orderService.getOrderPaymentById(id);

        if (orderPaymentDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderPaymentDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/byCard/{type}/{number}")
    public ResponseEntity<List<OrderDto>> getOrderByCard(@PathVariable String type, @PathVariable String number) {
        log.info("************************************* order-by-card");

        List<OrderDto> orderDtoList = orderService.getOrderByCard(type, number);

        if (orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/lookup", method = RequestMethod.POST)
    public ResponseEntity<OrderDetailDto> lookupOrderNo(@RequestParam String invoiceNo) {
        log.info("************************************* lookupOrderNo");

        OrderDetailDto orderDetailDto = orderService.getOrderNoForInvoiceNo(invoiceNo);
        if (orderDetailDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<OrderDetailDto>(orderDetailDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/resubmit", method = RequestMethod.POST)
    public ResponseEntity<StatusDto> resubmitOrder(@RequestParam String orderNo) {
        log.info("************************************* resubmitOrder");

        StatusDto statusDto = orderService.resubmitOrder(Long.valueOf(orderNo));
        if (statusDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<StatusDto>(statusDto, HttpStatus.OK);
    }

}
