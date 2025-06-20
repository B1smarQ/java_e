package org.forum.service;

import org.forum.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import org.forum.entity.Session;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionService {
    
    private SessionRepository sRepository;

    public boolean isValidToken(String token){
        try{
            Session session = this.sRepository.findByToken(token); 
            if(token == null){
                return false;
            }
            Timestamp currTime = new Timestamp(System.currentTimeMillis());

            if(currTime.after(session.getExpiryTime())){
                System.out.println("INVALID TOKEN TIME");
                return false;
            }
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Integer getUserIdFromToken(String token) {
        try {
            Session session = this.sRepository.findByToken(token);
            if (session == null) {
                return null;
            }
            return session.getUserId();
        } catch (Exception e) {
            return null;
        }
    }
}
