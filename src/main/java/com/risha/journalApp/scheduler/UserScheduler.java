package com.risha.journalApp.scheduler;

import com.risha.journalApp.cache.AppCache;
import com.risha.journalApp.entity.JournalEntry;
import com.risha.journalApp.entity.User;
import com.risha.journalApp.enums.Sentiment;
import com.risha.journalApp.model.SentimentData;
import com.risha.journalApp.repository.UserRepositoryImpl;
import com.risha.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepository.getUSerForSA();
        for(User user : users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment : sentiments){
                if(sentiment != null){
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;

            int maxCount = 0;

            for(Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if(mostFrequentSentiment != null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days" + mostFrequentSentiment).build();
                try {
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                }
                catch(Exception e){
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment data for previous week" ,sentimentData.getSentiment());
                }
            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void clearAppCache(){
        appCache.init();
    }
}
