package org.example.web.controller.system;

import org.example.common.core.domain.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysLoginController {
    @PostMapping
    public CommonResult<String> login(){

    }
}
