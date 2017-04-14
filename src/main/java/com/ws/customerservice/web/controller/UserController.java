package com.ws.customerservice.web.controller;

import java.util.List;

import com.ws.customerservice.dto.users.DepartmentDto;
import com.ws.customerservice.dto.users.TShirtDto;
import com.ws.customerservice.dto.users.UserDto;
import com.ws.customerservice.model.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ws.customerservice.service.UserService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/login")
    public ResponseEntity<UserDto> loginUser(@RequestParam String login, @RequestParam String pw) {
        log.info("************************************* user-login");

        UserDto userDto = userService.logUserIn(login, pw);
        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        /**
        GiftcardDto giftcardDto = giftcardService.shredGiftcard(gcNumber);
        if(giftcardDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
         **/
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value="/departments")
    public ResponseEntity<List> getDepartmentsList() {
        log.info("************************************* user-getDepartmentsList");

        List<DepartmentDto> departmentDtoList = userService.getAllDepartments();
        /**
        if(userDtoList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
         **/
        return new ResponseEntity<List>(departmentDtoList, HttpStatus.OK);
    }


    @RequestMapping(value="/all")
    public ResponseEntity<List> getAllusers() {
        log.info("************************************* user-getAllUsers");

        List<UserDto> userDtoList = userService.getAllActiveUsers();
        if(userDtoList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List>(userDtoList, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        log.info("************************************* user-getUserById");

        int agentId = Integer.parseInt(id);
        UserDto userDto = userService.getUserById(agentId);
        if(userDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveTShirtOrder", method = RequestMethod.POST)
    public ResponseEntity<TShirtDto> saveTShirtOrder(@RequestBody TShirtDto tShirtDto) {
        log.info("************************************* saveTShirtOrder");
        TShirtDto tShirtDto1 = null;
        try {
            tShirtDto1 = userService.saveTShirtOrder(tShirtDto);
        } catch (CustomerServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<TShirtDto>(tShirtDto1, HttpStatus.OK);
    }

}

 
