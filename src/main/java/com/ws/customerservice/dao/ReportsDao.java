package com.ws.customerservice.dao;

import com.ws.customerservice.dto.reports.*;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * ----------------------------------------------------------------------------
 * - Title:  ReportsDao
 * - Description:  This class handles the Data Access for the Reports Module
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.reports
 * - @date: 3/30/16
 * - @version $Rev$
 * -    3/30/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Repository
public class ReportsDao {

    @Qualifier("getEMSProdJdbcTemplate")
    @Autowired
    private JdbcTemplate emsProdJdbcTemplate;

    @Qualifier("getORMSJdbcTemplate")
    @Autowired
    private JdbcTemplate ormsJdbcTemplate;

    public List<ReportGrossDemandDto> daoGetGrossDemandReport() {

        List<ReportGrossDemandDto> reportGrossDemandDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetGrossDemandReport ]=-");

        //TODO: add start/end dates to this report
        try {
            reportGrossDemandDtoList = emsProdJdbcTemplate.execute(" { call app_report_gross_demand()}",
                    (CallableStatementCallback<List<ReportGrossDemandDto>>) callableStatement -> {
                        //callableStatement.setLong(1, orderId);
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportGrossDemandDto> reportGrossDemandDtoList1 = new ArrayList<>();
                        while (rs.next()) {
                            ReportGrossDemandDto reportGrossDemandDto = new ReportGrossDemandDto();
                            reportGrossDemandDto.setNumOrders(rs.getInt("orders"));
                            reportGrossDemandDto.setOrderDate(rs.getTimestamp("order_date"));
                            reportGrossDemandDto.setMerchandiseTotal(rs.getBigDecimal("merch_total"));
                            BigDecimal ads = reportGrossDemandDto.getMerchandiseTotal().divide
                                    (BigDecimal.valueOf(reportGrossDemandDto.getNumOrders()), 2);
                            reportGrossDemandDto.setAdsTotal(ads);
                            reportGrossDemandDto.setShippingTotal(rs.getBigDecimal("shipping_total"));
                            reportGrossDemandDto.setTaxTotal(rs.getBigDecimal("tax_total"));
                            reportGrossDemandDto.setDiscountTotal(rs.getBigDecimal("discount_total"));
                            reportGrossDemandDto.setNumUnits(rs.getInt("total_units"));

                            reportGrossDemandDtoList1.add(reportGrossDemandDto);
                        }
                        return reportGrossDemandDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportGrossDemandDtoList;

    }

    public List<ReportAllowanceDto> daoGetAllowanceReport() {

        List<ReportAllowanceDto> reportAllowanceDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetAllowanceReport ]=-");

        try {
            reportAllowanceDtoList = emsProdJdbcTemplate.execute(" { call app_report_allowances()}",
                    (CallableStatementCallback<List<ReportAllowanceDto>>) callableStatement -> {
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportAllowanceDto> reportAllowanceDtoList1 = new ArrayList<>();
                        while (rs.next()) {
                            ReportAllowanceDto reportAllowanceDto = new ReportAllowanceDto();

                            reportAllowanceDto.setTransactionId(rs.getLong("transaction_id"));
                            reportAllowanceDto.setTransactionDate(rs.getDate("transaction_date"));
                            reportAllowanceDto.setOrderNumber(rs.getLong("order_id"));
                            reportAllowanceDto.setDescription(rs.getString("description"));
                            reportAllowanceDto.setTransactionAmount(rs.getBigDecimal("transaction_amount"));
                            reportAllowanceDto.setComments(rs.getString("comments"));

                            reportAllowanceDtoList1.add(reportAllowanceDto);
                        }
                        return reportAllowanceDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportAllowanceDtoList;
    }

    public ReportNetSalesDto daoGetNetSalesReport(Date startDate, Date endDate) {

        ReportNetSalesDto reportNetSalesDto = new ReportNetSalesDto();

        log.info("-=[ calling daoGetNetSalesReport with startDate: {} and endDate: {}]=-", startDate, endDate);

        try {
            reportNetSalesDto = emsProdJdbcTemplate.execute(" { call app_report_net_sales(?,?)}",
                    (CallableStatementCallback<ReportNetSalesDto>) callableStatement -> {
                        callableStatement.setDate(1, startDate);
                        callableStatement.setDate(2, endDate);
                        ResultSet rs = callableStatement.executeQuery();
                        ReportNetSalesDto reportNetSalesDto1 = new ReportNetSalesDto();
                        while (rs.next()) {
                            reportNetSalesDto1.setSalesValue(rs.getBigDecimal("@Sales_value"));
                            reportNetSalesDto1.setSalesNumber(rs.getInt("@Sales_order_number"));
                            reportNetSalesDto1.setSalesUnits(rs.getInt("@Sales_units"));

                            reportNetSalesDto1.setReturnsValue(rs.getBigDecimal("@Returns_value"));
                            reportNetSalesDto1.setReturnsNumber(rs.getInt("@Returns_number"));
                            reportNetSalesDto1.setReturnUnits(rs.getInt("@Returns_units"));

                            reportNetSalesDto1.setAllowanceValue(rs.getBigDecimal("@allowances_value"));
                            reportNetSalesDto1.setAllowanceNumber(rs.getInt("@allowances_number"));

                            // calculate Net values here?

                            reportNetSalesDto1.setGiftCardValue(rs.getBigDecimal("@giftCard_value"));
                            reportNetSalesDto1.setGiftCardNumber(rs.getInt("@giftCard_number"));

                            reportNetSalesDto1.setLoyaltyValue(rs.getBigDecimal("@FBC_value"));
                            reportNetSalesDto1.setLoyaltyNumber(rs.getInt("@FBC_number"));

                            reportNetSalesDto1.setPaypalValue(rs.getBigDecimal("@paypal_value"));
                            reportNetSalesDto1.setPaypalNumber(rs.getInt("@paypal_number"));

                            reportNetSalesDto1.setCbaAmazonValue(rs.getBigDecimal("@cba_amazon_value"));
                            reportNetSalesDto1.setCbaAmazonNumber(rs.getInt("@cba_amazon_number"));
                        }
                        return reportNetSalesDto1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportNetSalesDto;
    }

    public List<ReportShippingDto> daoGetShippingReport(Date startDate, Date endDate) {

        List<ReportShippingDto> reportShippingDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetShippingReport with startDate: {} and endDate: {}]=-", startDate, endDate);

        try {
            reportShippingDtoList = emsProdJdbcTemplate.execute(" { call app_report_shipping_info(?,?)}",
                    (CallableStatementCallback<List<ReportShippingDto>>) callableStatement -> {
                        callableStatement.setDate(1, startDate);
                        callableStatement.setDate(2, endDate);
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportShippingDto> reportShippingDtoList1 = new ArrayList<ReportShippingDto>();
                        while (rs.next()) {
                            ReportShippingDto reportShippingDto = new ReportShippingDto();
                            reportShippingDto.setEmsOrderNo(rs.getLong("order_no"));
                            reportShippingDto.setShippingFirstName(rs.getString("shipto_first_name"));
                            reportShippingDto.setShippingMiddleI(rs.getString("shipto_middle_int"));
                            reportShippingDto.setShippingLastName(rs.getString("shipto_last_name"));
                            reportShippingDto.setShippingAddress(rs.getString("shipto_address"));
                            reportShippingDto.setShippingAddress2(rs.getString("shipto_address2"));
                            reportShippingDto.setShippingCity(rs.getString("shipto_city"));
                            reportShippingDto.setShippingState(rs.getString("shipto_state"));
                            reportShippingDto.setShippingZip(rs.getString("shipto_zip"));
                            reportShippingDto.setShippingCountry(rs.getString("shipto_country"));
                            reportShippingDto.setShippingAmount(rs.getBigDecimal("ship_amount"));
                            reportShippingDto.setDiscountShip(rs.getBigDecimal("discount_ship"));
                            reportShippingDto.setShipTrackingNo(rs.getString("ship_tracking_no"));
                            reportShippingDto.setShipCarrier(rs.getString("ship_carrier"));
                            reportShippingDto.setDateShipped(rs.getTimestamp("date_shipped"));

                            reportShippingDtoList1.add(reportShippingDto);
                        }
                        return reportShippingDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportShippingDtoList;
    }

    public List<ReportCancelDto> daoGetCancellationsReport(Date startDate, Date endDate) {

        List<ReportCancelDto> reportCancelDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetReturnsReport with startDate: {} and endDate: {}]=-", startDate, endDate);

        try {
            reportCancelDtoList = emsProdJdbcTemplate.execute(" { call app_report_cancellations(?,?)}",
                    (CallableStatementCallback<List<ReportCancelDto>>) callableStatement -> {
                        callableStatement.setDate(1, startDate);
                        callableStatement.setDate(2, endDate);
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportCancelDto> reportCancelDtoList1 = new ArrayList<ReportCancelDto>();
                        while (rs.next()) {
                            ReportCancelDto reportCancelDto = new ReportCancelDto();
                            reportCancelDto.setReason(rs.getString("cancel_reason"));
                            reportCancelDto.setNumOrders(rs.getInt("orders"));
                            reportCancelDto.setNumUnits(rs.getInt("units"));
                            reportCancelDto.setValue(rs.getBigDecimal("value"));

                            reportCancelDtoList1.add(reportCancelDto);
                        }
                        return reportCancelDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportCancelDtoList;
    }

    public List<ReportTransactionDto> daoGetTransactionsReportCF() {

        List<ReportTransactionDto> reportTransactionDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetTransactionsReport ]=-");

        try {
            reportTransactionDtoList = emsProdJdbcTemplate.execute(" { call app_report_open_trans_cf()}",
                    (CallableStatementCallback<List<ReportTransactionDto>>) callableStatement -> {
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportTransactionDto> reportTransactionDtoList1 = populateReportTransactionList(rs);
                        return reportTransactionDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reportTransactionDtoList;
    }

    public List<ReportTransactionDto> daoGetTransactionsReportRF() {

        List<ReportTransactionDto> reportTransactionDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetTransactionsReport ]=-");

        try {
            reportTransactionDtoList = emsProdJdbcTemplate.execute(" { call app_report_open_trans_rf()}",
                    (CallableStatementCallback<List<ReportTransactionDto>>) callableStatement -> {
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportTransactionDto> reportTransactionDtoList1 = populateReportTransactionList(rs);
                        return reportTransactionDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reportTransactionDtoList;
    }

    public List<ReportHoldDto> daoGetHoldReport(Timestamp startDate, Timestamp endDate) {

        List<ReportHoldDto> reportHoldDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetHoldReport with startDate: {} and endDate: {}]=-", startDate, endDate);

        try {
            reportHoldDtoList = emsProdJdbcTemplate.execute(" { call app_report_released_holds(?,?)}",
                    (CallableStatementCallback<List<ReportHoldDto>>) callableStatement -> {
                        callableStatement.setTimestamp(1, startDate);
                        callableStatement.setTimestamp(2, endDate);
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportHoldDto> reportHoldDtoList1 = new ArrayList<ReportHoldDto>();
                        while (rs.next()) {
                            ReportHoldDto reportHoldDto = new ReportHoldDto();
                            reportHoldDto.setEmsOrderNo(rs.getLong("ems_order_no"));
                            reportHoldDto.setWebOrderNo(rs.getLong("web_order_no"));
                            reportHoldDto.setMerchTotal(rs.getBigDecimal("merch_total"));
                            reportHoldDto.setDateOnHold(rs.getTimestamp("date_on_hold"));
                            reportHoldDto.setDateOffHold(rs.getTimestamp("date_off_hold"));
                            reportHoldDto.setReasonOnHold(rs.getString("on_hold_reason"));
                            reportHoldDto.setReasonOffHold(rs.getString("off_hold_reason"));
                            reportHoldDto.setManualHold(rs.getBoolean("manual_hold"));

                            reportHoldDtoList1.add(reportHoldDto);
                        }
                        return reportHoldDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reportHoldDtoList;
    }

    public List<ReportRmaDto> daoGetRmaReport(Timestamp startDate, Timestamp endDate) {

        List<ReportRmaDto> reportRmaDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetRmaReport with startDate: {} and endDate: {}]=-", startDate, endDate);

        try {
            reportRmaDtoList = emsProdJdbcTemplate.execute(" { call app_report_rma(?,?)}",
                    (CallableStatementCallback<List<ReportRmaDto>>) callableStatement -> {
                        callableStatement.setTimestamp(1, startDate);
                        callableStatement.setTimestamp(2, endDate);
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportRmaDto> reportRmaDtoList1 = new ArrayList<ReportRmaDto>();
                        while (rs.next()) {
                            ReportRmaDto reportRmaDto = new ReportRmaDto();
                            reportRmaDto.setOrderDate(rs.getTimestamp("order_date"));
                            reportRmaDto.setOrderNo(rs.getLong("order_no"));
                            reportRmaDto.setName(rs.getString("name"));
                            reportRmaDto.setRmaNumber(rs.getString("rma"));
                            reportRmaDto.setMerchandiseTotal(rs.getBigDecimal("merch"));
                            reportRmaDto.setStatus(rs.getBoolean("status"));
                            reportRmaDto.setRmaDate(rs.getTimestamp("rma_date"));
                            reportRmaDto.setPaymentType(rs.getString("payment_type"));

                            reportRmaDtoList1.add(reportRmaDto);
                        }
                        return reportRmaDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportRmaDtoList;
    }

    public List<ReportReturnsDto> daoGetReturnsReport(Timestamp startDate, Timestamp endDate) {

        List<ReportReturnsDto> reportReturnsDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetReturnsReport with startDate: {} and endDate: {}]=-", startDate, endDate);

        try {
            reportReturnsDtoList = emsProdJdbcTemplate.execute(" { call app_report_returns(?,?)}",
                    (CallableStatementCallback<List<ReportReturnsDto>>) callableStatement -> {
                        callableStatement.setTimestamp(1, startDate);
                        callableStatement.setTimestamp(2, endDate);
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReportReturnsDto> reportReturnsDtoList1 = new ArrayList<ReportReturnsDto>();
                        while (rs.next()) {
                            ReportReturnsDto reportReturnsDto = new ReportReturnsDto();
                            reportReturnsDto.setStyle(rs.getString("style"));
                            reportReturnsDto.setColor(rs.getString("style_color_desc"));
                            reportReturnsDto.setQuantity(rs.getInt("style_qty"));
                            reportReturnsDto.setDescription(rs.getString("style_desc"));
                            reportReturnsDto.setReasonCode(rs.getInt("return_reason_code"));
                            reportReturnsDto.setReasonDesc(rs.getString("return_reason"));

                            reportReturnsDtoList1.add(reportReturnsDto);
                        }
                        return reportReturnsDtoList1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return reportReturnsDtoList;
    }

    public List<ReportStyleDto> daoGetStyleInfo() throws CustomerServiceException {
        List<ReportStyleDto> reportStyleDtoList = new ArrayList<>();

        try {
            String sql = "select distinct top 100 ris.style, ris.style_desc, ris.dept_name " +
                    "from retek_item_setup ris, inventory inv where ris.sku = inv.item " +
                    "and ris.clear_date is null and inv.quantity = 0" +
                    "order by ris.style;";
            log.info("-=[ Executing SQL: {} ]=-", sql);
            reportStyleDtoList = emsProdJdbcTemplate.query(sql, new ReportStyleDtoMapper());
        }
        catch (Exception e) {
            throw new CustomerServiceException(e);
        }

        return reportStyleDtoList;
    }

    private static final class ReportStyleDtoMapper implements RowMapper<ReportStyleDto> {
        public ReportStyleDto mapRow(ResultSet rs, int i) throws SQLException {
            ReportStyleDto reportStyleDto = new ReportStyleDto();
            reportStyleDto.setStyle(rs.getString("style"));
            reportStyleDto.setDescription(rs.getString("style_desc"));
            reportStyleDto.setDept(rs.getString("dept_name"));

            return reportStyleDto;
        }
    }

    public List<ReportStyleSkuDto> daoGetStyleSkuInfo(String style) throws CustomerServiceException {
        List<ReportStyleSkuDto> reportStyleSkuDtoList = new ArrayList<>();

        try {


            String sql = "select ris.sku, ris.colour_desc, ris.size_desc, ris.unit_cost, ris.unit_retail, " +
                    "ris.last_received, ris.last_allocated " +
                    "from retek_item_setup ris, inventory inv " +
                    "where ris.style = " + style +
                    " and ris.sku = inv.item " +
                    "and inv.quantity = 0";

            //log.info("-=[ Executing SQL: {} ]=-", sql);
            reportStyleSkuDtoList = emsProdJdbcTemplate.query(sql, new ReportStyleSkuDtoMapper());
        }
        catch (Exception e) {
            throw new CustomerServiceException(e);
        }

        return reportStyleSkuDtoList;
    }

    private static final class ReportStyleSkuDtoMapper implements RowMapper<ReportStyleSkuDto> {
        public ReportStyleSkuDto mapRow(ResultSet rs, int i) throws SQLException {
            ReportStyleSkuDto reportStyleSkuDto = new ReportStyleSkuDto();
            reportStyleSkuDto.setSku(rs.getString("sku"));
            reportStyleSkuDto.setColor(rs.getString("colour_desc"));
            reportStyleSkuDto.setSize(rs.getString("size_desc"));
            reportStyleSkuDto.setUnitCost(rs.getBigDecimal("unit_cost"));
            reportStyleSkuDto.setUnitRetail(rs.getBigDecimal("unit_retail"));
            reportStyleSkuDto.setLastReceived(rs.getTimestamp("last_received"));
            reportStyleSkuDto.setLastAllocated(rs.getTimestamp("last_allocated"));

            return reportStyleSkuDto;
        }
    }

    public List<ReportOrderBatchDto> daoGetOrderBatchInfo() {
        List<ReportOrderBatchDto> reportOrderBatchDtoList = new ArrayList<>();

        log.info("---=[ calling daoGetOrders with orderBatchInfo ]=---");

        String sql = "select id, date_created, qty as numOrders, pending, date_completed, nfi_file_generated, " +
                "nfi_file_name, datefilesentnfi as dateSentToNFI " +
                "from batch where pending <> 0 and NFI_File_Name is not null " +
                "and DateFileSentNFI is not null and DateFileSentNFI > getdate() - 10 " +
                "order by date_created desc;";
        log.info("-=[ Executing SQL: {} ]=-", sql);
        reportOrderBatchDtoList = emsProdJdbcTemplate.query(sql, new ReportOrderBatchDtoMapper());

        return reportOrderBatchDtoList;
    }

    private static final class ReportOrderBatchDtoMapper implements RowMapper<ReportOrderBatchDto> {
        public ReportOrderBatchDto mapRow(ResultSet rs, int i) throws SQLException {
            ReportOrderBatchDto reportOrderBatchDto = new ReportOrderBatchDto();

            reportOrderBatchDto.setBatchId(rs.getInt("id"));
            reportOrderBatchDto.setDateCreated(rs.getTimestamp("date_created"));
            reportOrderBatchDto.setNumOrders(rs.getInt("numOrders"));
            reportOrderBatchDto.setPendingOrders(rs.getInt("pending"));
            reportOrderBatchDto.setDateCompleted(rs.getTimestamp("date_completed"));
            reportOrderBatchDto.setNfiFileGenerated(rs.getBoolean("nfi_file_generated"));
            reportOrderBatchDto.setNfiFilename(rs.getString("nfi_file_name"));
            reportOrderBatchDto.setDateSentToNfi(rs.getTimestamp("dateSentToNFI"));

            return reportOrderBatchDto;
        }
    }

    public ShipMethodDto daoGetShipMethodsForBatch(int batchId) {

        ShipMethodDto shipMethodDto = new ShipMethodDto();

        //log.info("-=[ calling daoGetShipMethodsForBatch id: {}]=-", batchId);

        try {
            shipMethodDto = emsProdJdbcTemplate.execute(" { call get_batch_shipment_counts(?)}",
                    (CallableStatementCallback<ShipMethodDto>) callableStatement -> {
                        callableStatement.setInt(1, batchId);
                        ResultSet rs = callableStatement.executeQuery();
                        ShipMethodDto shipMethodDto1 = new ShipMethodDto();
                        while (rs.next()) {
                            shipMethodDto1.setNextDay(rs.getInt("Next"));
                            shipMethodDto1.setSecondDay(rs.getInt("Second"));
                            shipMethodDto1.setGround(rs.getInt("Ground"));
                        }
                        return shipMethodDto1;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return shipMethodDto;
    }

    /**
     * query to lookup invoice numbers listed in a specific order batch file
     * <p>
     * select invoice_no from ord_invoice where order_no in
     * (select order_no from ord_order where batch_id in
     * (select id from batch where NFI_File_Name = 'OrderBatch_.....xml'))
     */
    public List<String> daoGetInvoiceNumbersForBatch(String batchFilename) {

        List<String> invoiceItemList = new ArrayList<>();

        try {
            String sql = "select invoice_no from ord_invoice where order_no in " +
                    "(select order_no from ord_order where batch_id in " +
                    "(select id from batch where NFI_File_Name = '" + batchFilename + "'))";

            RowMapper<String> mapper = new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String sku = rs.getString(("sku"));
                    return sku;
                }
            };
            log.info("SQL: {}", sql);
            invoiceItemList = emsProdJdbcTemplate.query(sql, mapper);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return invoiceItemList;

    }

    public List<ReportInventoryDto> daoGetItemsForStyle(String style) throws CustomerServiceException {

        List<ReportInventoryDto> reportInventoryDtoList = new ArrayList<>();

        try {

            log.info("---=[ calling daoGetItemsForStyle with style: {} ]=---", style);

            String sql = "select SKU, STYLE, STYLE_DESC, COLOUR, COLOUR_DESC, SIZE_DESC, CLEAR_DATE, CLEAR_REASON, " +
                    "LAST_RECEIVED, LAST_ALLOCATED " +
                    "from retek_item_setup where style = " + style;
            log.info("-=[ Executing SQL: {} ]=-", sql);
            reportInventoryDtoList = emsProdJdbcTemplate.query(sql, new ReportInventoryDtoMapper());

            // if the list is not null, then query RMS for the NFI inventory information
            if (reportInventoryDtoList.size() > 0) {
                for (ReportInventoryDto reportInventoryDto : reportInventoryDtoList) {
                    List<ReportInventoryDto> updatedList = daoGetNFIInventoryInfo(reportInventoryDto);
                    // list should have 1 item, get it out and update the reportInventoryDto
                    log.info("updatedList");
                    if (updatedList.size() > 0) {
                        reportInventoryDto = updatedList.get(0);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }

        return reportInventoryDtoList;
    }

    private static final class ReportInventoryDtoMapper implements RowMapper<ReportInventoryDto> {
        public ReportInventoryDto mapRow(ResultSet rs, int i) throws SQLException {
            ReportInventoryDto reportInventoryDto = new ReportInventoryDto();

            reportInventoryDto.setStyle(rs.getString("style"));
            reportInventoryDto.setItem(rs.getString("sku"));
            reportInventoryDto.setStyleDesc(rs.getString("style_desc"));
            reportInventoryDto.setColor(rs.getString("colour_desc"));
            reportInventoryDto.setColorNo(rs.getInt("colour"));
            reportInventoryDto.setSize(rs.getString("size_desc"));
            // check for null on the clearance fields
            Timestamp date = rs.getTimestamp("clear_date");
            if (date != null) {
                reportInventoryDto.setClearanceDate(date);
                reportInventoryDto.setClearanceReason(rs.getString("clear_reason"));
            }
            reportInventoryDto.setLastReceivedDate(rs.getTimestamp("last_received"));
            reportInventoryDto.setLastAllocatedDate(rs.getTimestamp("last_allocated"));

            return reportInventoryDto;
        }
    }


    public List<ReportInventoryDto> daoGetNFIInventoryInfo(ReportInventoryDto reportInventoryDto)
            throws CustomerServiceException {

        Calendar today = Calendar.getInstance();
        java.sql.Date prevMonth = new Date(today.getTimeInMillis());
        final String date = "TO_DATE('" + prevMonth + "', 'YYYY-MM-DD')";

        List<ReportInventoryDto> reportInventoryDtoList = new ArrayList<>();
        try {
            String sql = "SELECT ITEM, ON_HAND_QTY, OPEN_ORDER_QTY, INVENTORY_TYPE from ormsprd.wsl_wms_inventory_in " +
                    "where location = 980 AND ITEM = '" + reportInventoryDto.getItem() + "' AND TRUNC(run_date) = " + date;

            RowMapper<ReportInventoryDto> mapper = (rs, rowNum) -> {
                //ReportInventoryDto reportInventoryDto = new ReportInventoryDto();
                //reportInventoryDto.setItem(rs.getString("item"));
                reportInventoryDto.setNfiOnHandQty(rs.getInt("on_hand_qty"));
                reportInventoryDto.setNfiOpenOrderQty(rs.getInt("open_order_qty"));
                reportInventoryDto.setNfiInventoryType(rs.getString("inventory_type"));
                return reportInventoryDto;
            };
            log.info("SQL: {}", sql);
            reportInventoryDtoList = ormsJdbcTemplate.query(sql, mapper);

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
        return reportInventoryDtoList;
    }

    private List<ReportTransactionDto> populateReportTransactionList(ResultSet rs) throws SQLException {

        List<ReportTransactionDto> reportTransactionDtoList = new ArrayList();
        while (rs.next()) {
            ReportTransactionDto reportTransactionDto = new ReportTransactionDto();
            reportTransactionDto.setId(rs.getLong("transaction_id"));
            reportTransactionDto.setDate(rs.getTimestamp("transaction_date"));
            reportTransactionDto.setOrderNo(Long.valueOf(rs.getString("order_id")));
            reportTransactionDto.setType(rs.getString("transaction_type"));
            reportTransactionDto.setStatus(rs.getString("transaction_status"));
            reportTransactionDto.setAmount(rs.getBigDecimal("transaction_amount"));
            reportTransactionDto.setAccountNum(rs.getString("account_num"));

            reportTransactionDtoList.add(reportTransactionDto);
        }

        return reportTransactionDtoList;
    }

}
