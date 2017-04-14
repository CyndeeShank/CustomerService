package com.ws.customerservice.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.ws.services.commonutil.model.Response;
import com.ws.services.commonutil.model.MailInfo;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.web.controller
 * - @date: 10/13/16
 * - @version $Rev$
 * -    10/13/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    private String url = "https://pvtsvcs.wetseal.com/MailService/dispatcher/sendEmail";

    @RequestMapping(value = "/testShipConfirmEmail")
    public
    @ResponseBody
    Response testShipConfirmEmail() {

        log.info("-=[ calling testShipConfirmEmail ]=-");
        Response response = new Response();
        try {

            MailInfo mailInfo = new MailInfo();
            mailInfo.setToEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setFromEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setBccEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setEmailSubject("Test Shipping Confirmation Email");
            mailInfo.setEmailType(MailInfo.MAIL_TYPE.XML);
            mailInfo.setBodyText("<order order_no=\"813572\" web_order_no=\"5279443\" invoice_no=\"6655443\" company_id=\"980\" " +
                    "ship_firstname=\"Elizabeth\" ship_lastname=\"McGrew\" ship_address=\"205 E Clark " +
                    "St\" ship_address2=\"Apt 203\" ship_city=\"Champaign\" ship_state=\"IL\" " +
                    "ship_zip=\"61820\" ship_country=\"USA\" ship_email=\"\" " +
                    "bill_firstname=\"Elizabeth\" bill_lastname=\"McGrew\" bill_address=\"1041 Old " +
                    "Colony Rd\" bill_address2=\"\" bill_city=\"Lake Forest\" bill_state=\"IL\" " +
                    "bill_zip=\"60045\" bill_country=\"USA\" bill_email=\"emcgrew2@uiuc.edu\" " +
                    "ship_carrier=\"FedEx\" ship_tracking_no=\"937867322382\" gift_box=\"No\">\n" +
                    "  <lineitmes>\n" +
                    "    <lineitem sku=\"381968800001\" style_id=\"38196706\" color_id=\"123\" " +
                    "style_desc=\"Seamless Tube Bra\" color_desc=\"NUDE\" size_desc=\"S/M\" " +
                    "qty_ordered=\"1\" qty_shipped=\"1\" unit_price=\"5.50\" total_amt=\"5.50\" discount=\"0" +
                    ".00\" ship_code=\"1\" note=\"shipped\" />\n" +
                    "    <lineitem sku=\"387932000002\" style_id=\"38793189\" color_id=\"760\" " +
                    "style_desc=\"Stud Hoop &amp; Heart Trio Earring Set \" color_desc=\"GOLD\" " +
                    "size_desc=\"NS\" qty_ordered=\"1\" qty_shipped=\"1\" unit_price=\"6.00\" total_amt=\"6.00\"" +
                    " discount=\"0.00\" ship_code=\"1\" note=\"shipped\" />\n" +
                    "    <lineitem sku=\"389965900007\" style_id=\"38996573\" color_id=\"100\" " +
                    "style_desc=\"All over Lace Thong \" color_desc=\"WHITE\" size_desc=\"S\" " +
                    "qty_ordered=\"4\" qty_shipped=\"3\" unit_price=\"3.50\" total_amt=\"10.50\" discount=\"0" +
                    ".00\" ship_code=\"-1\" note=\"partial shipment\" />\n" +
                    "    <lineitem sku=\"390660800004\" style_id=\"39066077\" color_id=\"210\" " +
                    "style_desc=\"Get Your Green On Tote \" color_desc=\"NATURAL\" size_desc=\"NS\" " +
                    "qty_ordered=\"1\" qty_shipped=\"1\" unit_price=\"9.50\" total_amt=\"9.50\" discount=\"0" +
                    ".00\" ship_code=\"1\" note=\"shipped\" />\n" +
                    "    <lineitem sku=\"391155200002\" style_id=\"39115508\" color_id=\"110\" " +
                    "style_desc=\"Embroidered Flutter Sleeve Dress \" color_desc=\"IVORY\" " +
                    "size_desc=\"S\" qty_ordered=\"1\" qty_shipped=\"1\" unit_price=\"34.50\" total_amt=\"34" +
                    ".50\" discount=\"0.00\" ship_code=\"1\" note=\"shipped\" />\n" +
                    "    <lineitem sku=\"391997500001\" style_id=\"39199652\" color_id=\"110\" " +
                    "style_desc=\"Canvas Donut Wedge Heel \" color_desc=\"IVORY\" size_desc=\"shoe 10\" " +
                    "qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"24.50\" total_amt=\"0.00\" discount=\"0" +
                    ".00\" ship_code=\"0\" note=\"out of stock\" />\n" +
                    "    <lineitem sku=\"392630400009\" style_id=\"39262998\" color_id=\"684\" " +
                    "style_desc=\"Bow Front Eyelet Dress \" color_desc=\"PINK SLUSH\" size_desc=\"S\" " +
                    "qty_ordered=\"1\" qty_shipped=\"1\" unit_price=\"26.50\" total_amt=\"26.50\" discount=\"0" +
                    ".00\" ship_code=\"1\" note=\"shipped\" />\n" +
                    "    <lineitem sku=\"392985900001\" style_id=\"39298546\" color_id=\"542\" " +
                    "style_desc=\"Flutter Sleeve Surplice Dress\" color_desc=\"LIQUID TURQ\" " +
                    "size_desc=\"S\" qty_ordered=\"1\" qty_shipped=\"1\" unit_price=\"16.50\" total_amt=\"16" +
                    ".50\" discount=\"0.00\" ship_code=\"1\" note=\"shipped\" />\n" +
                    "  </lineitmes>\n" +
                    "  <payment merchandise_total=\"133.50\" discount=\"13.35\" rewards_dollars=\"0.00\"" +
                    " amt_after_disc=\"120.15\" tax=\"9.28\" ship_amt=\"0.00\" ship_method=\"Ground (5-7" +
                    " business days)\" total_amt=\"129.43\" />\n" +
                    "</order>");
            mailInfo.setXsltLocation("/fshare/jobs/ecom/shipment/xsl/");
            mailInfo.setXsltTemplate("order_ship_confirm.xsl");
            mailInfo.setHtmlLocation("/fshare/jobs/ecom/shipment/html/");
            mailInfo.setHtmlFilename("test-ship-confirm.html");
            mailInfo.setNeedAttachment(false);

            HttpEntity<MailInfo> httpEntity = new HttpEntity<>(mailInfo);

            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, httpEntity, Response.class);
            log.info("-=[ responseEntity: {} ]=-", responseEntity);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response.setResponseCode(Response.RESPONSE_CODE_SUCCESS);
                response.setResponseMessage("Successfully sent test shipping confirmation email");
            } else {
                response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
                response.setResponseMessage("Unable to send test shipping confirmation email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
            response.setResponseMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/testOrderCancelEmail")
    public
    @ResponseBody
    Response testOrderCancelEmail() {

        log.info("-=[ calling testOrderCancelEmail ]=-");
        Response response = new Response();
        try {

            MailInfo mailInfo = new MailInfo();
            mailInfo.setToEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setFromEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setBccEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setEmailSubject("Test Order Cancellation Email");
            mailInfo.setEmailType(MailInfo.MAIL_TYPE.XML);
            mailInfo.setBodyText("<order order_no=\"815210\" web_order_no=\"5282055\" company_id=\"980\" ship_firstname=\"Memuna\" " +
                    "ship_lastname=\"Amadu\" ship_address=\"6416 Edsall Rd\" ship_city=\"Alexandria\" ship_state=\"VA\" ship_zip=\"22312\" " +
                    "ship_email=\"\" bill_firstname=\"Henry\" bill_lastname=\" Anderson\" bill_address=\"1525 W 22nd Place\" " +
                    "bill_city=\"Los Angeles\" bill_state=\"CA\" bill_zip=\"90007\" bill_email=\"soulwiner15@yahoo.com\" " +
                    "ship_carrier=\"NA\" gift_box=\"No\" " +
                    "cancel_reason_code=\"05\" " +
                    "cancel_reason_desc=\"This email is to notify you that your order has been cancelled due to your credit card being declined. We apologize for any inconvenience.\">\n" +
                    "  <lineitmes>\n" +
                    "    <lineitem sku=\"386285200008\" style_id=\"38628399\" color_id=\"226\" style_desc=\"Open Toe Satin Ruffle Heel\" color_desc=\"CHAMPAGNE\" size_desc=\"shoe 7.5\" qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"26.50\" total_amt=\"26.50\" discount=\"0.00\" note=\"\" />\n" +
                    "    <lineitem sku=\"386287000002\" style_id=\"38628399\" color_id=\"400\" style_desc=\"Open Toe Satin Ruffle Heel\" color_desc=\"PURPLE\" size_desc=\"shoe 7.5\" qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"26.50\" total_amt=\"26.50\" discount=\"0.00\" note=\"\" />\n" +
                    "    <lineitem sku=\"389792500005\" style_id=\"38889134\" color_id=\"205\" style_desc=\"Mary Jane Wedge\" color_desc=\"CHOCOLATE\" size_desc=\"shoe 7.5\" qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"26.50\" total_amt=\"26.50\" discount=\"0.00\" note=\"\" />\n" +
                    "    <lineitem sku=\"390435400002\" style_id=\"39043405\" color_id=\"760\" style_desc=\"Metallic Lace Skimmer\" color_desc=\"GOLD\" size_desc=\"shoe 8\" qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"15.00\" total_amt=\"15.00\" discount=\"0.00\" note=\"\" />\n" +
                    "    <lineitem sku=\"391997100003\" style_id=\"39199652\" color_id=\"110\" style_desc=\"Canvas Donut Wedge Heel \" color_desc=\"IVORY\" size_desc=\"shoe 8\" qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"24.50\" total_amt=\"24.50\" discount=\"0.00\" note=\"\" />\n" +
                    "    <lineitem sku=\"391998000005\" style_id=\"39199652\" color_id=\"404\" style_desc=\"Canvas Donut Wedge Heel \" color_desc=\"ROSE\" size_desc=\"shoe 8\" qty_ordered=\"1\" qty_shipped=\"0\" unit_price=\"24.50\" total_amt=\"24.50\" discount=\"0.00\" note=\"\" />\n" +
                    "  </lineitmes>\n" +
                    "  <payment merchandise_total=\"143.50\" discount=\"0.00\" rewards_dollars=\"0.00\" " +
                    "amt_after_disc=\"176.89\" tax=\"8.44\" ship_amt=\"24.95\" ship_method=\"Next Day (1 business day)\" total_amt=\"176.89\" />\n" +
                    "</order> ");

            mailInfo.setXsltLocation("/fshare/jobs/ecom/cancel/xsl/");
            mailInfo.setXsltTemplate("order_cancel.xsl");
            mailInfo.setHtmlLocation("/fshare/jobs/ecom/cancel/html/");
            mailInfo.setHtmlFilename("test-order-cancel.html");
            mailInfo.setNeedAttachment(false);

            HttpEntity<MailInfo> httpEntity = new HttpEntity<>(mailInfo);

            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, httpEntity, Response.class);
            log.info("-=[ responseEntity: {} ]=-", responseEntity);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response.setResponseCode(Response.RESPONSE_CODE_SUCCESS);
                response.setResponseMessage("Successfully sent test order cancellation email");
            } else {
                response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
                response.setResponseMessage("Unable to send test order cancellation email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
            response.setResponseMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/testOrderRefundEmail")
    public
    @ResponseBody
    Response testOrderRefundEmail() {

        log.info("-=[ calling testOrderRefundEmail ]=-");
        Response response = new Response();
        try {

            MailInfo mailInfo = new MailInfo();
            mailInfo.setToEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setFromEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setBccEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setEmailSubject("Wet Seal Refund Order#: 5235737");
            mailInfo.setEmailType(MailInfo.MAIL_TYPE.XML);
            mailInfo.setBodyText(" <order order_no=\"786960\" web_order_no=\"5235737\" invoice_no=\"334455\" company_id=\"980\" ship_firstname=\"Devinearth\" ship_lastname=\"Williams\"\n" +
                    "ship_address=\"4213 robbins ave\" ship_address2=\"\" ship_city=\"Philadelphia\" ship_state=\"PA\" ship_zip=\"19135\"\n" +
                    "ship_country=\"USA\" ship_email=\"devinewilliams@comcast.net\" bill_firstname=\"Devinearth\" bill_lastname=\"Williams\"\n" +
                    "bill_address=\"4213 robbins ave\" bill_address2=\"\" bill_city=\"Philadelphia\" bill_state=\"PA\" bill_zip=\"19135\"\n" +
                    "bill_country=\"USA\" bill_email=\"devinewilliams@comcast.net\" ship_carrier=\"FedEx\" ship_tracking_no=\"937867322382\"\n" +
                    "gift_box=\"No\" refund_amt=\"106.50\"/>");
            mailInfo.setXsltLocation("/fshare/jobs/ecom/refund/xsl/");
            mailInfo.setXsltTemplate("order_refund.xsl");
            mailInfo.setHtmlLocation("/fshare/jobs/ecom/refund/html/");
            mailInfo.setHtmlFilename("test-order-refund.html");
            mailInfo.setNeedAttachment(false);

            HttpEntity<MailInfo> httpEntity = new HttpEntity<>(mailInfo);

            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, httpEntity, Response.class);
            log.info("-=[ responseEntity: {} ]=-", responseEntity);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response.setResponseCode(Response.RESPONSE_CODE_SUCCESS);
                response.setResponseMessage("Successfully sent test order refund email");
            } else {
                response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
                response.setResponseMessage("Unable to send test order refund email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
            response.setResponseMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/testOrderReturnEmail")
    public
    @ResponseBody
    Response testOrderReturnEmail() {

        log.info("-=[ calling testOrderReturnEmail ]=-");
        Response response = new Response();
        try {

            MailInfo mailInfo = new MailInfo();
            mailInfo.setToEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setFromEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setBccEmailAddress("cyndee.shank@wetseal.com");
            mailInfo.setEmailSubject("Wet Seal Return Order#: 5235737");
            mailInfo.setEmailType(MailInfo.MAIL_TYPE.XML);
            mailInfo.setBodyText("<order order_no=\"807197\" web_order_no=\"5269485\" invoice_no=\"334455\" company_id=\"980\" ship_firstname=\"Jen\" ship_lastname=\"Thomas\"\n" +
                    "       ship_address=\"26972 Burbank\" ship_address2=\"\" ship_city=\"Foothill Ranch\" ship_state=\"CA\" ship_zip=\"92610\"\n" +
                    "       ship_country=\"USA\" ship_email=\"\" bill_firstname=\"Jennifer\" bill_lastname=\"Thomas\" bill_address=\"310 15th St\"\n" +
                    "       bill_address2=\"# 5\" bill_city=\"Huntington Beach\" bill_state=\"CA\" bill_zip=\"92648\" bill_country=\"USA\"\n" +
                    "       bill_email=\"jennifer.thomas@wetseal.com\" ship_carrier=\"NA\" gift_box=\"No\">\n" +
                    "    <lineitmes>\n" +
                    "        <lineitem sku=\"392520900008\" style_id=\"39252005\" color_id=\"622\" style_desc=\"One Shoulder Banded Bottom Top\"\n" +
                    "                  color_desc=\"CORAL ROSE\" size_desc=\"M\" qty_order=\"1\" qty_shipped=\"1\" qty_returned=\"1\"\n" +
                    "                  unit_price=\"17.50\" total_amt=\"17.50\" discount=\"0.00\" subtotal_amt=\"17.50\"\n" +
                    "                  date_processed=\"2008-02-28T23:12:00\"/>\n" +
                    "        <lineitem sku=\"390946700004\" style_id=\"39094551\" color_id=\"291\" style_desc=\"Yarn Dye Ruffle Trim Top\"\n" +
                    "                  color_desc=\"BITTERSWEET\" size_desc=\"M\" qty_order=\"1\" qty_shipped=\"1\" qty_returned=\"1\"\n" +
                    "                  unit_price=\"24.50\" total_amt=\"24.50\" discount=\"0.00\" subtotal_amt=\"24.50\"\n" +
                    "                  date_processed=\"2008-02-28T23:12:00\"/>\n" +
                    "        <lineitem sku=\"391524700003\" style_id=\"39152381\" color_id=\"716\" style_desc=\"Bow Dot Cami\" color_desc=\"LEMON ICE\"\n" +
                    "                  size_desc=\"M\" qty_order=\"1\" qty_shipped=\"1\" qty_returned=\"1\" unit_price=\"16.50\" total_amt=\"16.50\"\n" +
                    "                  discount=\"0.00\" subtotal_amt=\"16.50\" date_processed=\"2008-02-28T23:12:00\"/>\n" +
                    "    </lineitmes>\n" +
                    "</order> ");
            mailInfo.setXsltLocation("/fshare/jobs/ecom/return/xsl/");
            mailInfo.setXsltTemplate("order_return.xsl");
            mailInfo.setHtmlLocation("/fshare/jobs/ecom/return/html/");
            mailInfo.setHtmlFilename("test-order-return.html");
            mailInfo.setNeedAttachment(false);

            HttpEntity<MailInfo> httpEntity = new HttpEntity<>(mailInfo);

            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, httpEntity, Response.class);
            log.info("-=[ responseEntity: {} ]=-", responseEntity);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response.setResponseCode(Response.RESPONSE_CODE_SUCCESS);
                response.setResponseMessage("Successfully sent test order return email");
            } else {
                response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
                response.setResponseMessage("Unable to send test order return email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
            response.setResponseMessage(e.getMessage());
        }
        return response;
    }

    /**
    @RequestMapping(value = "/testShortShippedEmail")
    public
    @ResponseBody
    Response testOrderRefundEmail() {

        log.info("-=[ calling testOrderRefundEmail ]=-");
        Response response = new Response();
        try {
            HttpEntity<MailInfo> httpEntity = new HttpEntity<>(mailInfo);

            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, httpEntity, Response.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response.setResponseCode(Response.RESPONSE_CODE_SUCCESS);
                response.setResponseMessage("Successfully sent test short shipped email");
            } else {
                response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
                response.setResponseMessage("Unable to send test short shipped email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Response.RESPONSE_CODE_FAILURE);
            response.setResponseMessage(e.getMessage());
        }
        return response;
    }
    **/

}
