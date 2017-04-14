package com.ws.customerservice.dao;

import com.ws.customerservice.dto.users.DepartmentDto;
import com.ws.customerservice.dto.users.TShirtDto;
import com.ws.customerservice.dto.users.UserDto;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * - Title:  UserDao
 * - Description:  This is the DAO layer for the Customer Service application
 * - Copyright:  Copyright (c) 2016
 * - Company:  Wet Seal, LLC
 * - @author <a href="Cyndee.Shank@wetseal.com">Cyndee Shank</a>
 * - @package: com.ws.customerservice.dao
 * - @date: 3/4/16
 * - @version $Rev$
 * -    3/4/16 - Cyndee Shank - Created the file
 * --------------------------------------------------------------------------
 */
@Slf4j
@Repository
public class UserDao {

    @Qualifier("getEMSProdJdbcTemplate")
    @Autowired
    private JdbcTemplate emsJdbcTemplate;


    public UserDto daoLogUserIn(String login, String pw){
        UserDto userDto = new UserDto();

        return userDto;
    }

    public List<DepartmentDto> daoGetDepartments() {
        List<DepartmentDto> departmentDtoList = new ArrayList<>();

        String sql = "select distinct Dept from agents";
        log.info("-=[ Executing SQL: {} ]=-", sql);
        departmentDtoList = emsJdbcTemplate.query(sql, new DepartmentDtoMapper());

        return departmentDtoList;
    }

    private static final class DepartmentDtoMapper implements RowMapper<DepartmentDto> {
        public DepartmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setName(rs.getString("Dept"));
            return departmentDto;
        }
    }

    public List<UserDto> daoGetUsers() {
        List<UserDto> userDtoList = new ArrayList<>();

        String sql = "select agent_id, dept, first_name, last_name, description, login, password, active, " +
                "sysadmin, can_do_refunds from agents where active = 1";
        log.info("-=[ Executing SQL: {} ]=-", sql);
        userDtoList = emsJdbcTemplate.query(sql, new UserDtoMapper());

        return userDtoList;
    }

    public UserDto daoGetUserById(int agentId) {
        UserDto userDto = null;

        try
        {
            userDto = emsJdbcTemplate.execute(" { call get_agent_by_id(?)}",
                    (CallableStatementCallback<UserDto>) callableStatement -> {
                        callableStatement.setInt(1, agentId);
                        ResultSet rs = callableStatement.executeQuery();
                        UserDto userDto1 = new UserDto();
                        while (rs.next())
                        {
                            userDto1.setAgentId(rs.getInt("agent_id"));
                            userDto1.setFirstName(rs.getString("first_name"));
                            userDto1.setLastName(rs.getString("last_name"));
                            userDto1.setDept(rs.getString("Dept"));
                            userDto1.setExt(rs.getInt("ext"));
                            userDto1.setEmail(rs.getString("email"));
                            userDto1.setLogin(rs.getString("login"));
                            userDto1.setPassword(rs.getString("password"));
                            userDto1.setSysAdmin(rs.getBoolean("sysadmin"));
                            userDto1.setCanDoRefunds(rs.getBoolean("can_do_refunds"));
                        }
                        return userDto1;
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return userDto;
    }

    private static final class UserDtoMapper implements RowMapper<UserDto> {
        public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDto userDto = new UserDto();

            userDto.setAgentId(rs.getInt("agent_id"));
            userDto.setDept(rs.getString("dept"));
            userDto.setFirstName(rs.getString("first_name"));
            userDto.setLastName(rs.getString("last_name"));
            userDto.setLogin(rs.getString("login"));
            userDto.setPassword(rs.getString("password"));
            userDto.setActive(rs.getBoolean("active"));
            userDto.setSysAdmin(rs.getBoolean("sysadmin"));
            userDto.setCanDoRefunds(rs.getBoolean("can_do_refunds"));

            return userDto;
        }
    }

    public TShirtDto daoSaveTShirtOrder(TShirtDto tShirtDto) throws CustomerServiceException {
        try {
            String sql = "INSERT INTO [EMS].[dbo].employee_sale_events (employee_id,employee_name,employee_size,quantity) " +
            "VALUES (" + tShirtDto.getEmpno() + ", " +
                    "'" + tShirtDto.getName() + "'," +
                    "'" + tShirtDto.getSize() + "'," +
                    tShirtDto.getQty() + ")";

            log.info("SQL: {}", sql);
            int status = emsJdbcTemplate.update(sql);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomerServiceException(e);
        }
        return tShirtDto;
    }


}
