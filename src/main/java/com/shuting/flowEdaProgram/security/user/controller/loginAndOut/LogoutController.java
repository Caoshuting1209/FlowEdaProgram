package com.shuting.flowEdaProgram.security.user.controller.loginAndOut;

import com.shuting.flowEdaProgram.commons.http.Result;

import com.shuting.flowEdaProgram.security.user.service.LoginAndOut.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @Autowired private LogoutService logoutService;

    @CrossOrigin
    @GetMapping("/logout")
    public Result<String> logout() {
        return logoutService.logout();
    }
}
