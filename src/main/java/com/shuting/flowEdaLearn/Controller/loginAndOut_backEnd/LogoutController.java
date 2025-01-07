package com.shuting.flowEdaLearn.Controller.loginAndOut_backEnd;

import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.service.LoginAndOut_backEnd.LogoutService;
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
