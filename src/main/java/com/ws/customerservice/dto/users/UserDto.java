package com.ws.customerservice.dto.users;

import java.util.Date;
import lombok.Data;

@Data
public class UserDto {

    private int agentId;
    private String dept;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private Date dateNewPassword;
    private String email;
    private int ext;
    private Boolean active;
    private Boolean sysAdmin;
    private Boolean canDoRefunds;
}
