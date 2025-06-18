package org.forum.service;

import lombok.AllArgsConstructor;
import org.forum.entity.Log;
import org.forum.entity.LogLevels;
import org.forum.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LogService {
    private LogRepository logRepository;

    public List<Log> getLogs(){
        return this.logRepository.findAll();
    }

    public List<Log> getLogsByLevel(LogLevels level){
        return this.logRepository.findByLogLevel(level.getLogLevel());
    }

    public void saveLog(Log log){
        this.logRepository.save(log);
    }
}
