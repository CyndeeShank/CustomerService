package com.ws.customerservice.service;

import com.ws.customerservice.dao.ReviewCustomerDao;
import com.ws.customerservice.dto.review.ReviewCustomerDto;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.service
 * - @date: 8/5/16
 * - @version $Rev$
 * -    8/5/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Service
public class ReviewCustomerService {

    @Autowired
    private ReviewCustomerDao reviewCustomerDao;

    public List<ReviewCustomerDto> getReviewFraudList() {
        List<ReviewCustomerDto> reviewCustomerDtoList = reviewCustomerDao.daoGetReviewCustomerList();

        return reviewCustomerDtoList;
    }

    public ReviewCustomerDto saveReviewCustomer(ReviewCustomerDto reviewCustomerDto) throws CustomerServiceException {
        try {
            int status = reviewCustomerDao.daoSaveReviewCustomer(reviewCustomerDto);
            reviewCustomerDto.setStatus(status);
            //reviewCustomerDao.daoSaveReviewCustomer(reviewCustomerDto);
        } catch (CustomerServiceException e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
        return reviewCustomerDto;
    }
}
