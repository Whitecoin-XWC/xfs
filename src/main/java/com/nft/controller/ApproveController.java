package com.nft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description: nft
 * Created by moloq on 2021/9/3 10:01
 */
@RestController
public class ApproveController {

    @GetMapping("/approve")
    public String approve() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }
}
