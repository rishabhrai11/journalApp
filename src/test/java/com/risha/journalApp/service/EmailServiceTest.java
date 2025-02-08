package com.risha.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        emailService.sendEmail("rishabhrai1111rocks@gmail.com",
                "Testing Java mail Service",
                "Hello, awaz aa rhi h ?");
    }
}
