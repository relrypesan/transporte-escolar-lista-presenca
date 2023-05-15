package me.relrypesan.transporteescolarlistapresenca.adapters.web.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equalsIgnoreCase("user1234")) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(username, "$2a$10$OSUtl.Zyl7Q5b8rgkAawx.wfBWIMef3cqSaksTozCYlYbJY6Juo5i", new ArrayList<>());
    }
}
