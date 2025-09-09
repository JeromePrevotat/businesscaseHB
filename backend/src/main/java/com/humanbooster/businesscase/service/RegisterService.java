package com.humanbooster.businesscase.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.humanbooster.businesscase.model.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RegisterService {
    private final int CODE_LENGTH = 6;
    private final MailSender mailSender;

    public String generateConfirmationCode(){
        StringBuilder code = new StringBuilder(this.CODE_LENGTH);
        int i = 0;
        while (i < this.CODE_LENGTH){
            code.append((int)(Math.random() * 10));
            i++;
        }
        return code.toString();
    }

    public void sendConfirmationCode(User user) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("Confirmation Code");
        msg.setText("Welcome " + user.getUsername() + ", your confirmation code is: " + user.getValidationCode());
        try {
			this.mailSender.send(msg);
		}
		catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
    }

    public boolean validateCode(User user, String code){
        if (user.getValidationCode().equals(code)) {
            user.setAccountValid(true);
            return true;
        }
        return false;
    }
}
