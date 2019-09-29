package com.kzh.sys.service.sys.security;


import com.kzh.sys.dao.UserDao;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private UserDao userDao;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(user.getRole());
        user.setAuthorities(grantedAuths);
        return user;
    }

    public static Set<GrantedAuthority> obtionGrantedAuthorities(Role role) {
        Set<GrantedAuthority> authSet = new HashSet<>();
        authSet.add(new SimpleGrantedAuthority(role.getRoleKey()));
        return authSet;
    }
}