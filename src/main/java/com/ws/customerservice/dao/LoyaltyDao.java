package com.ws.customerservice.dao;

import com.ws.customerservice.dto.loyalty.CustomerDto;
import com.ws.customerservice.dto.loyalty.LoyaltyDto;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dao
 * - @date: 6/21/16
 * - @version $Rev$
 * -    6/21/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Repository
@Slf4j
public class LoyaltyDao {

    @Qualifier("getXBRJdbcTemplate")
    @Autowired
    private JdbcTemplate xbrJdbcTemplate;

    @Qualifier("getEMSJdbcTemplate")
    @Autowired
    private JdbcTemplate emsJdbcTemplate;

    @Qualifier("getORMSJdbcTemplate")
    @Autowired
    private JdbcTemplate ormsJdbcTemplate;

    public List<LoyaltyDto> daoLookupByNumber(String number) {
        List<LoyaltyDto> loyaltyDtoList = new ArrayList<>();
        try {
            String sql = "select first_name, last_name, address1, city, " +
                    "state_province, postal_code, e_mail_address, phone, " +
                    "fbc_expiration_date, fbc_account_num " +
                    "from xbr_customer_tab " +
                    "where fbc_account_num like '%" + number + "%' " +
                    "order by fbc_expiration_date desc";

            log.info("SQL: {}", sql);
            loyaltyDtoList = xbrJdbcTemplate.query(sql, new LoyaltyDtoMapper());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loyaltyDtoList;
    }

    private static final class LoyaltyDtoMapper implements RowMapper<LoyaltyDto> {
        public LoyaltyDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            LoyaltyDto loyaltyDto = new LoyaltyDto();

            loyaltyDto.setFirst(rs.getString("first_name"));
            loyaltyDto.setLast(rs.getString("last_name"));
            /**
             loyaltyDto.setAddress(rs.getString("address1"));
             loyaltyDto.setCity(rs.getString("city"));
             loyaltyDto.setState(rs.getString("state_province"));
             loyaltyDto.setZip(rs.getString("postal_code"));
             loyaltyDto.setEmail(rs.getString("e_mail_address"));
             loyaltyDto.setPhone(rs.getString("phone"));
             loyaltyDto.setLoyaltyNo(rs.getString("fbc_account_num"));
             loyaltyDto.setExpTimestamp(rs.getTimestamp("fbc_expiration_date"));
             loyaltyDto.setExpirationDate(new java.sql.Date(loyaltyDto.getExpTimestamp().getTime()));
             **/
            return loyaltyDto;
        }
    }

    public List<LoyaltyDto> daoLookupByName(String firstName, String lastName) {
        List<LoyaltyDto> loyaltyDtoList = new ArrayList<>();
        try {
            String sql = "select first_name, last_name, address1, city, fbc_account_num, state_province, " +
                    "postal_code, e_mail_address, phone, fbc_expiration_date " +
                    "from xbr_customer_tab " +
                    "where first_name like '%" + firstName + "%' AND " +
                    "last_name like '%" + lastName + "%'";

            log.info("SQL: {}", sql);
            loyaltyDtoList = xbrJdbcTemplate.query(sql, new LoyaltyDtoMapper());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loyaltyDtoList;
    }

    /**
     * PROCEDURE SEARCH_CUSTOMERS (
     * i_fbc_number    IN varchar2,
     * i_first_name    IN varchar2,
     * i_last_name     IN varchar2,
     * i_email_address IN varchar2,
     * i_search_type   IN varchar2,
     * o_customer_cursor OUT CUSTOMER_CURSOR);
     * <p>
     * TYPE CUSTOMER_OBJECT IS RECORD (
     * customer_id         VARCHAR2 (30),
     * last_name           VARCHAR2 (40),
     * first_name          VARCHAR2 (40),
     * email_address      VARCHAR2 (100),
     * sign_up_store       NUMBER,
     * fbc_account_num     VARCHAR2(12),
     * fbc_expiration_date DATE );
     **/
    public List<CustomerDto> daoLookupLoyaltyCustomers(LoyaltyDto loyaltyDto) {

        final String f_number = loyaltyDto.getNumber();
        final String f_first = loyaltyDto.getFirst();
        final String f_last = loyaltyDto.getLast();
        final String f_email = loyaltyDto.getEmail();
        final String f_search = loyaltyDto.getSearchType();

        List customerDtoList = (List) xbrJdbcTemplate.execute
                (connection -> {
                    CallableStatement callableStatement = connection.prepareCall
                            ("{call WSL_ECOM_SERVICE.SEARCH_CUSTOMERS(?,?,?,?,?,?)}");
                    callableStatement.setString(1, f_number);
                    callableStatement.setString(2, f_first);
                    callableStatement.setString(3, f_last);
                    callableStatement.setString(4, f_email);
                    callableStatement.setString(5, f_search);
                    //callableStatement.registerOutParameter(6, Types.REF_CURSOR, "WSL_ECOM_SERVICE.CUSTOMER_CURSOR");
                    callableStatement.registerOutParameter(6, OracleTypes.CURSOR);
                    return callableStatement;
                },
                        new CallableStatementCallback<List<CustomerDto>>() {
                            @Override
                            public List<CustomerDto> doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                                callableStatement.execute();
                                ArrayList resultList = new ArrayList();
                                ResultSet resultSet = (ResultSet) callableStatement.getObject(6);
                                while (resultSet.next()) {
                                    CustomerDto customerDto = new CustomerDto();
                                    customerDto.setCustomerId(resultSet.getString("customer"));
                                    customerDto.setLastName(resultSet.getString("last_name"));
                                    customerDto.setFirstName(resultSet.getString("first_name"));
                                    customerDto.setEmailAddress(resultSet.getString("e_mail_address"));
                                    customerDto.setSignUpStore(resultSet.getInt("sign_up_store"));
                                    customerDto.setLoyaltyNumber(resultSet.getString("fbc_account_num"));
                                    customerDto.setExpirationDate(resultSet.getDate("fbc_expiration_date"));
                                    customerDto.setExpTimestamp(resultSet.getTimestamp("fbc_expiration_date"));
                                    customerDto.setBirthDate(resultSet.getDate("birth_date"));
                                    customerDto.setBdTimestamp(resultSet.getTimestamp("birth_date"));
                                    resultList.add(customerDto);
                                }
                                return resultList;
                            }
                        });
        return customerDtoList;
    }

    public void daoDeactivateLoyaltyCard(String number) throws CustomerServiceException {

        try {

            xbrJdbcTemplate.execute("{call WSL_ECOM_SERVICE.DEACTIVATE_CARD(?)}",
                    new CallableStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInCallableStatement (CallableStatement callableStatement) throws
                                SQLException, DataAccessException {
                            callableStatement.setString(1, number);

                            return callableStatement.execute();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
    }

    public void daoSaveLoyaltyCustomer(CustomerDto customerDto, boolean newCustomer) throws CustomerServiceException {

        /**
         *    PROCEDURE SAVE_CUSTOMER(
         i_customer_id         IN varchar2,
         i_first_name          IN varchar2,
         i_last_name           IN varchar2,
         i_fbc_number          IN varchar2,
         i_fbc_expiration_date IN DATE,
         i_email_address       IN varchar2,
         i_birth_date          IN DATE,
         i_sign_up_store       IN NUMBER,
         i_new_customer        IN NUMBER);
         */
        log.info("-=[ CustomerDto:  ]=-");
        log.info("- customerId: {} -", customerDto.getCustomerId());
        log.info("- firstName: {} -", customerDto.getFirstName());
        log.info("- lastName: {} -", customerDto.getLastName());
        log.info("- loyalty#: {} -", customerDto.getLoyaltyNumber());
        log.info("- exp date: {} -", customerDto.getExpirationDate());
        log.info("- email address: {} -", customerDto.getEmailAddress());
        log.info("- birth date: {} -", customerDto.getBirthDate());
        log.info("- signUpStore: {} -", customerDto.getSignUpStore());
        String pattern = "MM/dd/YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String expDate = sdf.format(new Date(customerDto.getExpirationDate().getTime()));
        String birthDate = sdf.format(new Date(customerDto.getBirthDate().getTime()));
        final int newC;
        if (newCustomer) {
            newC = 1;
        }
        else {
            newC = 0;
        }

        try {

            xbrJdbcTemplate.execute("{call WSL_ECOM_SERVICE.SAVE_CUSTOMER(?,?,?,?,?,?,?,?,?)}",
                    new CallableStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInCallableStatement (CallableStatement callableStatement) throws
                                SQLException, DataAccessException {
                            callableStatement.setString(1, customerDto.getCustomerId());
                            callableStatement.setString(2, customerDto.getFirstName());
                            callableStatement.setString(3, customerDto.getLastName());
                            callableStatement.setString(4, customerDto.getLoyaltyNumber());
                            callableStatement.setString(5, expDate);
                            callableStatement.setString(6, customerDto.getEmailAddress());
                            callableStatement.setString(7, birthDate);
                            callableStatement.setInt(8, customerDto.getSignUpStore());
                            callableStatement.setInt(9, newC);

                            return callableStatement.execute();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
    }
    public List<CustomerDto> daoLookupLoyaltyCustomersOld(String first, String last, String number, String email,
                                                          CustomerDto.SEARCH_TYPE searchType) {

        final String f_number = number;
        final String f_first = first;
        final String f_last = last;
        final String f_email = email;
        final String f_search = searchType.name();

        final CallableStatement[] cs = new CallableStatement[1];
        List<CustomerDto> customerDtoList = (List) xbrJdbcTemplate.execute(con -> {
            Map typeMap = con.getTypeMap();
            con.setTypeMap(typeMap);

            try {
                typeMap.put("WSL_ECOM_SERVICE.CUSTOMER_OBJECT", Class.forName(CustomerDto.class.getName()));
            } catch (ClassNotFoundException var4) {
                log.error("Class not found exception");
            }

            cs[0] = con.prepareCall("{call WSL_ECOM_SERVICE.SEARCH_CUSTOMERS(?,?,?,?,?,?)}");
            cs[0].setString(1, f_number);
            cs[0].setString(2, f_first);
            cs[0].setString(3, f_last);
            cs[0].setString(4, f_email);
            cs[0].setString(5, f_search);
            //cs[0].registerOutParameter(6, Types.JAVA_OBJECT);
            cs[0].registerOutParameter(6, Types.ARRAY, "WSL_ECOM_SERVICE.CUSTOMER_CURSOR");
            return cs[0];
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException {
                ArrayList resultList = new ArrayList();
                cs.execute();
                Object[] dataList = (Object[]) ((Object[]) ((Array) cs.getObject(6)).getArray());
                Object[] arr$ = dataList;
                int len$ = dataList.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    Object rec = arr$[i$];
                    resultList.add((CustomerDto) rec);
                }

                return resultList;
            }
        });
        return customerDtoList;
    }
}

