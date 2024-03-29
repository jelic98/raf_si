package com.auth.domain.dao;

import com.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {
    public User findByUsername(String username);
    public void deleteById(String username);
}
