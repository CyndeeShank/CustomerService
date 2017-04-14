package com.ws.customerservice.service;

import com.ws.customerservice.dao.UserDao;
import com.ws.customerservice.dto.users.DepartmentDto;
import com.ws.customerservice.dto.users.TShirtDto;
import com.ws.customerservice.dto.users.UserDto;
import com.ws.customerservice.model.CustomerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userdao;

    public UserDto logUserIn(String login, String pw) {

        UserDto userDto = userdao.daoLogUserIn(login, pw);

        return userDto;
    }

    public List<DepartmentDto> getAllDepartments() {
        List<DepartmentDto> departmentDtoList = userdao.daoGetDepartments();
        return departmentDtoList;
    }

    public List<UserDto> getAllActiveUsers() {
        List<UserDto> userDtoList = userdao.daoGetUsers();
        return userDtoList;
    }

    public UserDto getUserById(int id) {
        UserDto userDto = userdao.daoGetUserById(id);

        return userDto;
    }

    public TShirtDto saveTShirtOrder(TShirtDto tShirtDto) throws CustomerServiceException {
        TShirtDto tShirtDto1 = userdao.daoSaveTShirtOrder(tShirtDto);

        return tShirtDto1;
    }
}
