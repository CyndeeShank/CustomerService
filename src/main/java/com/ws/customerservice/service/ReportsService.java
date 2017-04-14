package com.ws.customerservice.service;

import com.ws.customerservice.dao.ReportsDao;
import com.ws.customerservice.dto.reports.*;
import com.ws.customerservice.model.CustomerServiceException;
import com.ws.customerservice.model.DateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  Reports Service
 * - Description:  This class handles the service calls for the reports
 * -        section of the Customer Service Portal Application
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.service
 * - @date: 6/14/16
 * - @version $Rev$
 * -    6/14/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Service
public class ReportsService {

    @Autowired
    private ReportsDao reportsDao;

    public List<ReportGrossDemandDto> getGrossDemand() {

        List<ReportGrossDemandDto> reportGrossDemandDtoList = reportsDao.daoGetGrossDemandReport();

        return reportGrossDemandDtoList;
    }

    public String getGrossDemandFile(List<ReportGrossDemandDto> reportGrossDemandDtoList) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YY");
        Calendar today = Calendar.getInstance();
        String currentDate = formatter.format(today.getTime());
        String filename = "Gross Demand " + currentDate + ".xlsx";

        createGrossDemandSpreadsheet(reportGrossDemandDtoList, filename);

        return filename;
    }

    public List<ReportAllowanceDto> getAllowances() {

        List<ReportAllowanceDto> reportAllowanceDtoList = reportsDao.daoGetAllowanceReport();

        return reportAllowanceDtoList;
    }

    public ReportNetSalesDto getNetSales(DateDto dateDto) {

        java.sql.Date startDate = new java.sql.Date(dateDto.getStartDate().getTime());
        java.sql.Date endDate = new java.sql.Date(dateDto.getEndDate().getTime());

        ReportNetSalesDto reportNetSalesDto = reportsDao.daoGetNetSalesReport(startDate, endDate);
        reportNetSalesDto.setStartDate(new Date(startDate.getTime()));
        reportNetSalesDto.setEndDate(new Date(startDate.getTime()));

        return reportNetSalesDto;
    }

    public List<ReportOrderBatchDto> getOrderBatch() {

        List<ReportOrderBatchDto> reportOrderBatchDtoList = reportsDao.daoGetOrderBatchInfo();

        /**
         * loop through the list and retrieve the numbers for Next, Second and Ground shipping
         * so they can be displayed at the 'batch' level
         */
        for (ReportOrderBatchDto reportOrderBatchDto : reportOrderBatchDtoList) {
            reportOrderBatchDto.setShipMethod(reportsDao.daoGetShipMethodsForBatch(reportOrderBatchDto.getBatchId()));
        }

        return reportOrderBatchDtoList;
    }

    public List<ReportShippingDto> getShippingReport(DateDto dateDto) {
        List<ReportShippingDto> reportShippingDtoList =
        reportsDao.daoGetShippingReport(new java.sql.Date(dateDto.getStartDate().getTime()),
                new java.sql.Date(dateDto.getEndDate().getTime()));

        return reportShippingDtoList;
    }

    public List<ReportCancelDto> getCancellationReport(DateDto dateDto) {
        List<ReportCancelDto> reportCancelDtoList =
                reportsDao.daoGetCancellationsReport(new java.sql.Date(dateDto.getStartDate().getTime()),
                        new java.sql.Date(dateDto.getEndDate().getTime()));

        return reportCancelDtoList;
    }

    public List<ReportReturnsDto> getReturnsReport(DateDto dateDto) {

        List<ReportReturnsDto> reportReturnsDtoList = reportsDao.daoGetReturnsReport(dateDto.getStartDate(), dateDto.getEndDate());

        return reportReturnsDtoList;
    }

    public List<ReportRmaDto> getRmaReport(DateDto dateDto) {

        List<ReportRmaDto> reportRmaDtoList = reportsDao.daoGetRmaReport(dateDto.getStartDate(), dateDto.getEndDate());
        return reportRmaDtoList;
    }

    public List<ReportHoldDto> getHoldReport(DateDto dateDto) {

        List<ReportHoldDto> reportHoldDtoList = reportsDao.daoGetHoldReport(dateDto.getStartDate(), dateDto.getEndDate());
        return reportHoldDtoList;
    }

    public List<ReportTransactionDto> getTransactionsCFReport() {

        // retrieve charge failed transactions
        List<ReportTransactionDto> reportTransactionDtoCFList = reportsDao.daoGetTransactionsReportCF();

        return reportTransactionDtoCFList;
    }

    public List<ReportTransactionDto> getTransactionsRFReport() {

        // retrieve refund failed transactions
        List<ReportTransactionDto> reportTransactionDtoRFList = reportsDao.daoGetTransactionsReportRF();

        return reportTransactionDtoRFList;
    }

    public List<ReportInventoryDto> getInventoryInfoForStyle(String style) {

        // retrieve the item info for the given style
        List<ReportInventoryDto> reportInventoryDtoList = null;
        try {
            reportInventoryDtoList = reportsDao.daoGetItemsForStyle(style);
            if (reportInventoryDtoList.size() < 1) {
                throw new CustomerServiceException("Style not found in Retek Item Setup");
            }
            else {
                // populate the image url for each item
                for (ReportInventoryDto reportInventoryDto : reportInventoryDtoList) {
                    reportInventoryDto.setImageUrl("https://cdn.wetseal.com/content/ws/product_images/" +
                            reportInventoryDto.getStyle() + reportInventoryDto.getColorNo() + "_th.jpg");
                }
            }

        }
        catch (CustomerServiceException e) {
            e.printStackTrace();
        }

        return reportInventoryDtoList;

    }

    public List<ReportStyleDto> getStyleSkuInfo() {

        List<ReportStyleDto> reportStyleDtoList = null;

        try {
            // retrieve the list of styles with at least one sku with no inventory
            reportStyleDtoList = reportsDao.daoGetStyleInfo();

            if (reportStyleDtoList.size() > 0) {

                for (ReportStyleDto reportStyleDto : reportStyleDtoList) {
                    reportStyleDto.setStyleSkuDtoList(reportsDao.daoGetStyleSkuInfo(reportStyleDto.getStyle()));
                }
            }
        }
        catch (CustomerServiceException e) {
            e.printStackTrace();
        }

        return reportStyleDtoList;
    }


    private void createGrossDemandSpreadsheet(List<ReportGrossDemandDto> reportGrossDemandDtoList, String filename) {

        try {
            // create a new workbook
            Workbook wb = new XSSFWorkbook();

            // workbook creation helper
            CreationHelper creationHelper = wb.getCreationHelper();

            // get the date (MM-YY) for the Sheet name
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            Calendar today = Calendar.getInstance();
            String currentDate = formatter.format(today.getTime());
            String name = "Gross Demand " + currentDate;

            // use utility to validate and/or update the sheet name if it has invalid characters
            String sheetName = WorkbookUtil.createSafeSheetName(name);

            // create a new sheet
            Sheet sheet = wb.createSheet(sheetName);

            // create the header row -- rows are 0 based
            Row headerRow = sheet.createRow((short) 0);

            // freeze the top row
            sheet.createFreezePane(0, 1, 0, 1);

            // create the header style
            CellStyle headerCellStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            headerCellStyle.setFont(font);
            headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            headerCellStyle.setWrapText(true);

            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue(creationHelper.createRichTextString("Date"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(1);
            headerCell.setCellValue(creationHelper.createRichTextString("Number of Orders"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(2);
            headerCell.setCellValue(creationHelper.createRichTextString("Merchandise Total"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(3);
            headerCell.setCellValue(creationHelper.createRichTextString("Discount Total"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(4);
            headerCell.setCellValue(creationHelper.createRichTextString("ADS"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(5);
            headerCell.setCellValue(creationHelper.createRichTextString("Shipping Total"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(6);
            headerCell.setCellValue(creationHelper.createRichTextString("Tax Total"));
            headerCell.setCellStyle(headerCellStyle);
            headerCell = headerRow.createCell(7);
            headerCell.setCellValue(creationHelper.createRichTextString("Total Units"));
            headerCell.setCellStyle(headerCellStyle);

            // Iterate through the list and put into spreadsheet
            int rowNum = 1;
            for (ReportGrossDemandDto reportGrossDemandDto : reportGrossDemandDtoList) {
                Row row = sheet.createRow(rowNum++);

                // create cell style for dates
                CellStyle dateCellStyle = wb.createCellStyle();
                dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("mm/dd/yy"));
                dateCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                // create cell style for centering columns
                CellStyle centerCellStyle = wb.createCellStyle();
                centerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                // date
                Cell cell = row.createCell(0);
                cell.setCellValue(reportGrossDemandDto.getOrderDate());
                cell.setCellStyle(dateCellStyle);

                // number of orders
                cell = row.createCell(1);
                cell.setCellValue(NumberFormat.getInstance().format(reportGrossDemandDto.getNumOrders()));
                cell.setCellStyle(centerCellStyle);

                // merchandise total
                cell = row.createCell(2);
                cell.setCellValue(NumberFormat.getCurrencyInstance().format(reportGrossDemandDto.getMerchandiseTotal()));
                cell.setCellStyle(centerCellStyle);

                // discount total
                cell = row.createCell(3);
                cell.setCellValue(NumberFormat.getCurrencyInstance().format(reportGrossDemandDto.getDiscountTotal()));
                cell.setCellStyle(centerCellStyle);

                // ads total
                cell = row.createCell(4);
                cell.setCellValue(NumberFormat.getCurrencyInstance().format(reportGrossDemandDto.getAdsTotal()));
                cell.setCellStyle(centerCellStyle);

                // shipping total
                cell = row.createCell(5);
                cell.setCellValue(NumberFormat.getCurrencyInstance().format(reportGrossDemandDto.getShippingTotal()));
                cell.setCellStyle(centerCellStyle);

                // tax total
                cell = row.createCell(6);
                cell.setCellValue(NumberFormat.getCurrencyInstance().format(reportGrossDemandDto.getTaxTotal()));
                cell.setCellStyle(centerCellStyle);

                // total units
                cell = row.createCell(7);
                cell.setCellValue(NumberFormat.getInstance().format(reportGrossDemandDto.getNumUnits()));
                cell.setCellStyle(centerCellStyle);
            }

            // autosize the columns
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
