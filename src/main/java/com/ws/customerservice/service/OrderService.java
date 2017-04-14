package com.ws.customerservice.service;

import com.ws.customerservice.dao.OrderDao;
import com.ws.customerservice.dto.order.*;
import com.ws.customerservice.model.StatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  Order Service
 * - Description:  This class contains the service calls for the Order portion
 * of the Customer Service Application
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.service
 * - @date: 5/20/16
 * - @version $Rev$
 * -    5/20/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    private List<OrderDto> currentOrderDtoList;

    public List<OrderDto> getNewOrders() {

        List<OrderDto> orderDtoList = orderDao.daoGetOrders(OrderDto.ORDER_STATUS.NEW);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public List<OrderDto> getDistributionOrders() {

        List<OrderDto> orderDtoList = orderDao.daoGetOrders(OrderDto.ORDER_STATUS.SENT_TO_DIST);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    /**
     * Test -- this method currently only returns my (cyndee's) order for testing purposes
     * @return
     */
    public List<OrderDto> getShippedOrders() {

        //List<OrderDto> orderDtoList = orderDao.daoGetOrders(OrderDto.ORDER_STATUS.SHIPPED);
        List<OrderDto> orderDtoList = orderDao.daoGetOrdersTest();
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public List<OrderDto> getAutoHoldOrders() {

        List<OrderDto> orderDtoList = orderDao.daoGetOrders(OrderDto.ORDER_STATUS.AUTO_HOLD);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public List<OrderDto> getManualHoldOrders() {

        List<OrderDto> orderDtoList = orderDao.daoGetOrders(OrderDto.ORDER_STATUS.MANUAL_HOLD);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public List<OrderDto> getCancelledOrders() {

        List<OrderDto> orderDtoList = orderDao.daoGetOrders(OrderDto.ORDER_STATUS.CANCELLED);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public List<OrderDto> searchOrders(OrderSearchDto orderSearchDto) {

        // add check for required fields based on searchType (number, cc, name, email)
        List<OrderDto> orderDtoList = orderDao.daoSearchOrders(orderSearchDto);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public List<OrderDto> getOrderByCard(String matchType, String number) {

        List<OrderDto> orderDtoList = orderDao.daoSearchOrderByCard(matchType, number);
        currentOrderDtoList = orderDtoList;
        return orderDtoList;
    }

    public OrderDetailDto getOrderDetailsById(Long orderId) {

        OrderDto currentOrderDto = new OrderDto();
        // figure out how to get the OrderDto object that was selected
        // so we can pre-populate those fields and just add the new detail fields
        for (OrderDto orderDto : currentOrderDtoList) {
            if (orderDto.getOrderNo().equals(orderId)) {
                BeanUtils.copyProperties(orderDto, currentOrderDto);
            }
        }
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        // copy the OrderDto values into the new orderDetailDto object
        if (currentOrderDto != null) {
            BeanUtils.copyProperties(currentOrderDto, orderDetailDto);
            orderDetailDto = orderDao.daoGetOrderDetailsById(orderId, orderDetailDto);
            List<OrderLineItemDto> orderLineItemDtoList = orderDao.daoGetOrderLineItemsById(orderId);
            orderDetailDto.setOrderLineItemDtoList(orderLineItemDtoList);
        }

        return orderDetailDto;
    }

    public List<OrderStatusDto> getOrderStatusById(Long orderId) {

        List<OrderStatusDto> orderStatusDtoList = orderDao.daoGetOrderStatusById(orderId);

        return orderStatusDtoList;
    }

    public List<OrderPaymentDto> getOrderPaymentById(Long orderId) {

        List<OrderPaymentDto> orderPaymentDtoList = orderDao.daoGetOrderPaymentById(orderId);

        return orderPaymentDtoList;
    }

    public OrderDetailDto getOrderNoForInvoiceNo(String invoiceNo) {

        Long orderNo = orderDao.daoGetOrderNoFromInvoiceNo(invoiceNo);

        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto = orderDao.daoGetOrderDetailsById(orderNo, orderDetailDto);
        orderDetailDto.setOrderNo(orderNo);
        orderDetailDto.setInvoiceNo(Long.valueOf(invoiceNo));
        List<OrderLineItemDto> orderLineItemDtoList = orderDao.daoGetOrderLineItemsById(orderNo);
        orderDetailDto.setOrderLineItemDtoList(orderLineItemDtoList);

        return orderDetailDto;
    }

    public StatusDto resubmitOrder(Long orderId) {

        StatusDto statusDto = orderDao.daoResubmitOrder(orderId);

        return statusDto;
    }

}
