package com.shuting.flowEdaLearn.Controller.loginAndOut;

import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.user.LoginUser;
import com.shuting.flowEdaLearn.service.LoginAndOut.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired private LoginService loginService;

    @CrossOrigin
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginUser loginUser) {
        return loginService.login(loginUser);
    }
}
