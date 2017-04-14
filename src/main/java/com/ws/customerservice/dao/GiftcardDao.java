package com.ws.customerservice.dao;

import com.ws.customerservice.dto.giftcard.GiftcardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dao
 * - @date: 3/28/16
 * - @version $Rev$
 * -    3/28/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Repository
public class GiftcardDao {

    @Qualifier("getEMSProdJdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GiftcardDto daoShredGiftcard(String gcNumber) {
        GiftcardDto giftcardDto = null;

        try {
            giftcardDto = jdbcTemplate.execute(" { call app_shred_gift_card(?) }",
                    (CallableStatementCallback<GiftcardDto>) callableStatement -> {
                        callableStatement.setString(1, gcNumber);
                        ResultSet rs = callableStatement.executeQuery();
                        GiftcardDto giftcardDto1 = new GiftcardDto();
                        while (rs.next()) {
                            giftcardDto1.setNumber(gcNumber);
                            int status = rs.getInt("Status");
                            int reason = rs.getInt("Reason");
                            if (status == 1) {
                                giftcardDto1.setStatus(GiftcardDto.GC_STATUS.SUCCESS);
                            }
                            else {
                                giftcardDto1.setStatus(GiftcardDto.GC_STATUS.FAIL);
                            }
                            switch (reason) {
                                case 1:
                                    giftcardDto1.setReason(GiftcardDto.GC_REASON.NOT_IN_SYSTEM);
                                    break;
                                case 2:
                                    giftcardDto1.setReason(GiftcardDto.GC_REASON.ALREADY_ACTIVATED);
                                    break;
                                case 3:
                                    giftcardDto1.setReason(GiftcardDto.GC_REASON.NOT_ACCESSED);
                                    break;
                                case 4:
                                    giftcardDto1.setReason(GiftcardDto.GC_REASON.HAS_ACTIVITY);
                                    break;
                            }
                        }
                        return giftcardDto1;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return giftcardDto;
    }
}
