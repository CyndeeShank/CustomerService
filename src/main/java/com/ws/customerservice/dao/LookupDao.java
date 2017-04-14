package com.ws.customerservice.dao;

import com.ws.customerservice.model.LookupDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Lookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  lookup Dao
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dao
 * - @date: 9/7/16
 * - @version $Rev$
 * -    9/7/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Repository
public class LookupDao {
    @Qualifier("getEMSProdJdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static List<String> invalidStateList;

    private static List<String> getInvalidStateList() {
        if (invalidStateList == null) {
            invalidStateList = new ArrayList<>();
            invalidStateList.add("PW");
            invalidStateList.add("PR");
            invalidStateList.add("VI");
            invalidStateList.add("FM");
            invalidStateList.add("MH");
            invalidStateList.add("GU");
            invalidStateList.add("AS");
            invalidStateList.add("MP");
        }
        return invalidStateList;
    }

    public List<LookupDto> daoGetStates() {
        List<LookupDto> lookupDtoList = new ArrayList<>();

        String sql = "select state, postal_code from state_abbreviations";
        log.info("-=[ Executing SQL: {} ]=-", sql);
        lookupDtoList = jdbcTemplate.query(sql, new LookupDtoMapper());

        // remove non-US states
        List<LookupDto> stateList = new ArrayList<>();
        for (LookupDto lookupDto : lookupDtoList) {
            if (!getInvalidStateList().contains(lookupDto.getAbbreviation())) {
                stateList.add(lookupDto);
            }
        }
        return stateList;
    }

    private static final class LookupDtoMapper implements RowMapper<LookupDto> {
        public LookupDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            LookupDto lookupDto = new LookupDto();
            lookupDto.setAbbreviation(rs.getString("postal_code"));
            lookupDto.setDescription(rs.getString("state"));
            return lookupDto;
        }
    }

}
