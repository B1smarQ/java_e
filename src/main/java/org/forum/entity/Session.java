package org.forum.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "session")
@AllArgsConstructor
@NoArgsConstructor
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "token")
  private String token;

  @Column(name = "created")
  private Timestamp created;

  @Column(name = "expiry_time")
  private Timestamp expiryTime;

}
