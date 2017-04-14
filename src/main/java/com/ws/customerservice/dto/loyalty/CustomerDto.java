package com.ws.customerservice.dto.loyalty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.sql.*;

/**
 * ----------------------------------------------------------------------------
 * - Title:  XXX
 * - Description:  This class does xxx for Repo
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dto.loyalty
 * - @date: 7/7/16
 * - @version $Rev$
 * -    7/7/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Data
public class CustomerDto implements Serializable, SQLData {

    private String customerId;
    private String lastName;
    private String firstName;
    private String emailAddress;
    private Integer signUpStore;
    private String loyaltyNumber;
    private Date expirationDate;
    private Date birthDate;
    private Timestamp expTimestamp;
    private Timestamp bdTimestamp;
    private SEARCH_TYPE searchType;

    private String typeName = "CUSTOMER_OBJECT";

    public String getSQLTypeName() throws SQLException {
        return this.typeName;
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.typeName = typeName;
        this.customerId = stream.readString();
        this.lastName = stream.readString();
        this.firstName = stream.readString();
        this.emailAddress = stream.readString();
        this.signUpStore = stream.readInt();
        this.loyaltyNumber = stream.readString();
        this.expirationDate = stream.readDate();
        this.birthDate = stream.readDate();
    }

    public void writeSQL(SQLOutput stream) throws SQLException {
    }

    public enum SEARCH_TYPE {
        NAME, NUMBER, EMAIL, ALL
    }

}


