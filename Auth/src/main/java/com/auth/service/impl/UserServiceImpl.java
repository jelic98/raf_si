package com.auth.service.impl;

import com.auth.domain.User;
import com.auth.domain.dao.UserDao;
import com.auth.domain.dto.UserReqDto;
import com.auth.domain.dto.UserResDto;
import com.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserResDto saveExistingUser(UserReqDto userReqDto) {
        User userToSave = new User(userReqDto);
        return saveAndReturnDTO(userToSave);
    }

    @Override
    public UserResDto createNewUser(UserReqDto userReqDto) {
        return saveAndReturnDTO(new User(userReqDto));
    }

    @Override
    public UserResDto findByUsername(String username) {
        return new UserResDto(userDao.findByUsername(username));
    }

    @Override
    public void deleteUserByUsername(String username) {
        userDao.deleteById(username);
    }

    private UserResDto saveAndReturnDTO(User user){

        userDao.save(user);
        UserResDto toReturn = new UserResDto(user);
        return toReturn;

    }
}
