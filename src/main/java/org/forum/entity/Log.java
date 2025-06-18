package org.forum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "log_level", length = 10)
    private String logLevel;
    
    @Column(name = "metadata", length = 256)
    private String description;

    @Column(name = "times_stamp",nullable = true)
    private Timestamp timestamp;
} 