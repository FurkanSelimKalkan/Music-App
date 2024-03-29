package com.application.music.handler;

import com.application.music.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlbumMessageListener {

    @Autowired
    private EmailService emailService;

    private String exampleUser = "exampleUser12U2ufds8gmail.com";
    private String text = "Ein neues Album wurde veröffenlticht: ";
    private String subject = "Neues Album verfügbar";
    @RabbitListener(queues = "newAlbumQueue")

    public void receiveMessage(String albumName) {
        System.out.println("Neues Album hinzugefügt: " + albumName);

        emailService.sendSimpleMessage(exampleUser, subject, text + albumName);
    }

}
