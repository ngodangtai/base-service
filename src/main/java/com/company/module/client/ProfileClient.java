package com.company.module.client;

import com.company.module.dto.CountryDto;
import com.company.module.configs.client.InternalInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "profileClient", url = "${service.profile.url}", configuration = { InternalInterceptor.class })
public interface ProfileClient {
//    @PostMapping("/login-key/{userid}")
//    LoginResponse getLoginkey(@PathVariable("userid") String userid, @RequestBody LoginReq loginReq);

    @GetMapping("/list-phone-code")
    List<CountryDto> getPhoneCodes();
}
