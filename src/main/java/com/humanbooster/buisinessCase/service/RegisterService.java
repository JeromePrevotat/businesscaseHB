package com.humanbooster.buisinessCase.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RegisterService {
    private final int CODE_LENGTH = 6;

    public String generateConfirmationCode(){
        StringBuilder code = new StringBuilder(this.CODE_LENGTH);
        int i = 0;
        while (i < this.CODE_LENGTH){
            code.append((int)(Math.random() * 10));
            i++;
        }
        return code.toString();
    }
}
