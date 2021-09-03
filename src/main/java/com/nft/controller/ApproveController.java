package com.nft.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description: nft
 * Created by moloq on 2021/9/3 10:01
 */
@RestController
@Slf4j
public class ApproveController {

    @PostMapping("/approve")
    public String approve(@RequestBody String message) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        log.info(message);
        return address.getHostAddress();
    }
}
