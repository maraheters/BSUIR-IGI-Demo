package com.sparkplug.sparkplugbackend.security.service;

import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SparkplugUserDetailsService implements UserDetailsService {

    private final SparkplugUsersRepository usersRepository;

    public SparkplugUserDetailsService(SparkplugUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SparkplugUser> userOptional = usersRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new SparkplugUserDetails(userOptional.get());
    }

}