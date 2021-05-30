package com.auth.ctrl;

import com.auth.domain.User;
import com.auth.domain.dto.LoginDto;
import com.auth.domain.dto.LoginResDto;
import com.auth.domain.dto.UserReqDto;
import com.auth.domain.dto.UserResDto;
import com.auth.service.AuthService;
import com.auth.service.TokenHandlerService;
import com.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserCtrl {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenHandlerService tokenHandlerService;

    @Autowired
    private AuthService authService;

    @PostMapping("")
    public UserResDto getElement(@ModelAttribute UserReqDto dto) {
        return userService.createNewUser(dto);
    }

    @PostMapping("/login")
    public LoginResDto loginUser(@ModelAttribute LoginDto dto){
        return authService.login(dto);
    }

    @GetMapping("")
    public UserResDto getElement(@RequestHeader String authorization) {
        String token = authorization;
        String username = tokenHandlerService.getUsernameByToken(token);
        return userService.findByUsername(username);
    }

    @GetMapping("/username")
    public String getUsername(@RequestHeader String authorization) {
        String token = authorization;
        String username = tokenHandlerService.getUsernameByToken(token);
        return username;
    }

    @PutMapping("")
    public UserResDto updateElement(@RequestHeader String authorization, @ModelAttribute UserReqDto dto) {
        String token = authorization;
        String password = dto.getPassword();
        String username = tokenHandlerService.getUsernameByToken(token);
        User currUser = new User(userService.findByUsername(username));
        currUser.setPassword(password);
        return userService.saveExistingUser(new UserReqDto(currUser));
    }

    @DeleteMapping("")
    //@Transactional
    public void deleteElement(@ModelAttribute String username){
        userService.deleteUserByUsername(username);
    }

    @PostMapping("/logout")
    public void logoutUser(@ModelAttribute LoginDto dto){

    }
}
