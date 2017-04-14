package com.ws.customerservice.dao;

import com.ws.customerservice.dto.review.ReviewCustomerDto;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dao
 * - @date: 8/5/16
 * - @version $Rev$
 * -    8/5/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Repository
public class ReviewCustomerDao {

    @Qualifier("getEMSProdJdbcTemplate")
    @Autowired
    private JdbcTemplate emsJdbcTemplate;

    public List<ReviewCustomerDto> daoGetReviewCustomerList() {
        List<ReviewCustomerDto> reviewCustomerDtoList = new ArrayList<>();

        log.info("-=[ calling daoGetReviewCustomerList ]=-");
        try {
            reviewCustomerDtoList = emsJdbcTemplate.execute(" { call app_get_review_list()}",
                    (CallableStatementCallback<List<ReviewCustomerDto>>) callableStatement -> {
                        ResultSet rs = callableStatement.executeQuery();
                        List<ReviewCustomerDto> reviewCustomerDtoList1 = new ArrayList<>();
                        while (rs.next()) {
                            ReviewCustomerDto reviewCustomerDto = new ReviewCustomerDto();
                            reviewCustomerDto.setId(rs.getInt("ID"));
                            reviewCustomerDto.setMatchValue(rs.getString("match_value"));
                            reviewCustomerDto.setDateCreated(rs.getDate("date_Created"));
                            String dataType = rs.getString("data_type");
                            switch (dataType) {
                                case "address":
                                    reviewCustomerDto.setFraudType(ReviewCustomerDto.FRAUD_TYPE.ADDRESS);
                                    break;
                                case "email":
                                    reviewCustomerDto.setFraudType(ReviewCustomerDto.FRAUD_TYPE.EMAIL);
                                    break;
                                case "zipcode":
                                    reviewCustomerDto.setFraudType(ReviewCustomerDto.FRAUD_TYPE.ZIPCODE);
                                    break;
                            }
                            reviewCustomerDto.setReason(rs.getString("reason"));
                            reviewCustomerDtoList1.add(reviewCustomerDto);
                        }
                        return reviewCustomerDtoList1;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviewCustomerDtoList;
    }

    public int daoSaveReviewCustomer(ReviewCustomerDto reviewCustomerDto) throws CustomerServiceException {

        int status;
        try {

            // change the name of the proc
            //emsJdbcTemplate.execute(" { call app_save_review_customer(?,?,?)}",
            status = (int) emsJdbcTemplate.execute(" { call app_save_review_customer(?,?,?,?)}",
                    new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement callableStatement)
                                throws SQLException, DataAccessException {
                            callableStatement.setString(1, reviewCustomerDto.getAddress());
                            callableStatement.setString(2, reviewCustomerDto.getZip());
                            callableStatement.setString(3, reviewCustomerDto.getEmail());
                            callableStatement.setString(4, reviewCustomerDto.getReason());
                            //int returnValue = callableStatement.executeUpdate();
                            ResultSet rs = callableStatement.executeQuery();
                            int statusValue = 1;
                            while (rs.next()) {
                                statusValue = rs.getInt("Status");
                            }
                            return statusValue;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
        return status;
    }
}
