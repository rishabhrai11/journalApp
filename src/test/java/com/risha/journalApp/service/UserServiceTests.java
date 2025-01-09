package com.risha.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserServiceTests {
    @Autowired
    private UserService userRepository;

    @Disabled
    @Test
    public void testFindByUsername() {
        assertNotNull(userRepository.findByUsername("Rishabh"));
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
            "1,1,3"
    })
    public void test(int a,int b,int expected){
        assertEquals(expected,a+b);
    }

}
