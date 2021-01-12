package com.example.security;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
@Slf4j
public class CumtomUserDetailService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("getList"));
        authorityList.add(new SimpleGrantedAuthority("add"));
        authorityList.add(new SimpleGrantedAuthority("update"));
        authorityList.add(new SimpleGrantedAuthority("delete"));
        CumtomUser cumtomUser = new CumtomUser("admin",passwordEncoder.encode("123456"),authorityList);
        log.info("用户登陆成功:{}", JSON.toJSONString(cumtomUser));
        return cumtomUser;
    }
}
