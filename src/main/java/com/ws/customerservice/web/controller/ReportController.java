package com.ws.customerservice.web.controller;

import com.ws.customerservice.config.AppConfig;
import com.ws.customerservice.dto.reports.*;
import com.ws.customerservice.model.DateDto;
import com.ws.customerservice.service.ReportsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  Report Controller
 * - Description:  This is the controller class for the Reports section of
 * --       The Customer Service Portal Application
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 3/30/16
 * - @version $Rev$
 * -    3/30/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private AppConfig appConfig;

    private List<ReportGrossDemandDto> currentGrossDemandList;
    // TEMP
    private ReportNetSalesDto currentNetSales;

    @RequestMapping(value = "/grossDemand")
    public ResponseEntity<List<ReportGrossDemandDto>> getGrossDemand() {
        log.info("************************************* reports-grossDemand");

        List<ReportGrossDemandDto> reportGrossDemandDtoList = reportsService.getGrossDemand();
        // save the current list for download functionality
        currentGrossDemandList = reportGrossDemandDtoList;

        if (reportGrossDemandDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportGrossDemandDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/grossDemand/export")
    @ResponseBody
    public ServletOutputStream downloadGrossDemand(HttpServletResponse response) {
        log.info("************************************* reports-grossDemand-export");

        // create the Gross Demand File
        String filename = reportsService.getGrossDemandFile(currentGrossDemandList);
        String path = appConfig.getLocalDirectory() + File.separator + filename;

        try {
            response.setContentType("application/xls");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
            FileCopyUtils.copy(in, response.getOutputStream());
            in.close();
            response.getOutputStream().flush();
            log.info("************************************* after flushing output stream");
            response.getOutputStream().close();
            log.info("************************************* after closing output stream");
            /**
            InputStream inputStream = new FileInputStream(filename);
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            response.setContentType("application/xls");
            response.flushBuffer();
             **/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return new FileSystemResource(reportsService.getGrossDemandFile(currentGrossDemandList));
        return null;
    }

    @RequestMapping(value = "/grossDemand/export/old")
    @ResponseBody
    public FileSystemResource downloadGrossDemandOld(HttpServletResponse response) {
        log.info("************************************* reports-grossDemand-export");

        return new FileSystemResource(reportsService.getGrossDemandFile(currentGrossDemandList));
    }

    @RequestMapping(value = "/netsales", method = RequestMethod.POST)
    public ResponseEntity<ReportNetSalesDto> getNetSales(@RequestBody DateDto dateDto) {
        log.info("************************************* reports-netsales");

        // TEMP
        if (currentNetSales == null) {
            ReportNetSalesDto reportNetSalesDto = reportsService.getNetSales(dateDto);

            if (reportNetSalesDto == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            currentNetSales = reportNetSalesDto;
        }
        return new ResponseEntity<>(currentNetSales, HttpStatus.OK);
    }

    @RequestMapping(value = "/allowances")
    public ResponseEntity<List<ReportAllowanceDto>> getAllowances() {
        log.info("************************************* reports-allowances");

        List<ReportAllowanceDto> reportAllowanceDtoList = reportsService.getAllowances();

        if (reportAllowanceDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportAllowanceDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/orderbatch")
    public ResponseEntity<List<ReportOrderBatchDto>> getOrderBatch() {
        log.info("************************************* reports-order-batch");

        List<ReportOrderBatchDto> reportOrderBatchDtoList = reportsService.getOrderBatch();

        if (reportOrderBatchDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportOrderBatchDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/returns", method = RequestMethod.POST)
    public ResponseEntity<List<ReportReturnsDto>> getReturns(@RequestBody DateDto dateDto) {
        log.info("************************************* reports-returns");

        List<ReportReturnsDto> reportReturnsDtoList = reportsService.getReturnsReport(dateDto);

        if (reportReturnsDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportReturnsDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/shipping", method = RequestMethod.POST)
    public ResponseEntity<List<ReportShippingDto>> getShippingInfo(@RequestBody DateDto dateDto) {
        log.info("************************************* reports-shipping");

        List<ReportShippingDto> reportShippingDtoList = reportsService.getShippingReport(dateDto);

        if (reportShippingDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportShippingDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/cancels", method = RequestMethod.POST)
    public ResponseEntity<List<ReportCancelDto>> getCancellations(@RequestBody DateDto dateDto) {
        log.info("************************************* reports-cancellations");

        List<ReportCancelDto> reportCancelDtoList = reportsService.getCancellationReport(dateDto);

        if (reportCancelDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportCancelDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/rma", method = RequestMethod.POST)
    public ResponseEntity<List<ReportRmaDto>> getRmaReport(@RequestBody DateDto dateDto) {
        log.info("************************************* reports-rmas");

        List<ReportRmaDto> reportRmaDtoList = reportsService.getRmaReport(dateDto);

        if (reportRmaDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportRmaDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/hold", method = RequestMethod.POST)
    public ResponseEntity<List<ReportHoldDto>> getHoldReport(@RequestBody DateDto dateDto) {
        log.info("************************************* reports-holds");

        List<ReportHoldDto> reportHoldDtoList = reportsService.getHoldReport(dateDto);

        if (reportHoldDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportHoldDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions/cf", method = RequestMethod.GET)
    public ResponseEntity<List<ReportTransactionDto>> getTransactionsCFReport() {
        log.info("************************************* reports-transactions-cf");

        List<ReportTransactionDto> reportTransactionDtoList = reportsService.getTransactionsCFReport();

        if (reportTransactionDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportTransactionDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions/rf", method = RequestMethod.GET)
    public ResponseEntity<List<ReportTransactionDto>> getTransactionsRFReport() {
        log.info("************************************* reports-transactions-rf");

        List<ReportTransactionDto> reportTransactionDtoList = reportsService.getTransactionsRFReport();

        if (reportTransactionDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportTransactionDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    public ResponseEntity<List<ReportInventoryDto>> getInventoryInfoForStyle(@RequestParam String style) {
        log.info("************************************* reports-inventory-style");

        List<ReportInventoryDto> reportInventoryDtoList = reportsService.getInventoryInfoForStyle(style);

        if (reportInventoryDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportInventoryDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/styles", method = RequestMethod.GET)
    public ResponseEntity<List<ReportStyleDto>> getStylesReport() {
        log.info("************************************* reports-styles");

        List<ReportStyleDto> reportStyleDtoList = reportsService.getStyleSkuInfo();

        if (reportStyleDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportStyleDtoList, HttpStatus.OK);
    }

}


