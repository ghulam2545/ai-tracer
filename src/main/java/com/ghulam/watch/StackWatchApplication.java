package com.ghulam.watch;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StackWatchApplication {

    private final ChatClient chatClient;

    public StackWatchApplication(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(StackWatchApplication.class, args);
    }

    @PostConstruct
    public void init() {
        String content = chatClient.prompt().user("How are you?").call().content();
        System.out.println(content);
    }
}
