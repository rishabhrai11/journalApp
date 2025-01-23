package com.risha.journalApp.controller;

import com.risha.journalApp.api.response.WeatherResponse;
import com.risha.journalApp.entity.User;
import com.risha.journalApp.repository.UserRepository;
import com.risha.journalApp.service.UserService;
import com.risha.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUsername(username);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greetings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Varanasi");
        String greet = "";
        if(weatherResponse != null){
            if(weatherResponse.getCurrent().getIsDay().equals("Yes")){
                greet = ", today looks like "+weatherResponse.getCurrent().getWeatherDescriptions().get(0);
            }
            else{
                greet = ", tonight looks like "+weatherResponse.getCurrent().getWeatherDescriptions().get(0);
            }
        }
        return new ResponseEntity<>("Hi, "+authentication.getName()+""+greet,HttpStatus.OK);
    }


}
