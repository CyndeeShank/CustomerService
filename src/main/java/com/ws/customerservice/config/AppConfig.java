package com.ws.customerservice.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.config
 * - @date: 6/15/16
 * - @version $Rev$
 * -    6/15/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Data
@Configuration
public class AppConfig {

    private String localDirectory;
    private String gcHistoryUrl = "https://svcs.wetseal.com/GiftCardService/dispatcher/historyAndBalance";
    private String gcActivateUrl = "https://svcs.wetseal.com/GiftCardService/dispatcher/activateEcomCard";
}
