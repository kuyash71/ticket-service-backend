package com.sau.ticket_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "worklogs")
public class Worklog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Many worklog -> one ticket
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_id", nullable = false)
  private Ticket ticket;

  // Keycloak-ready: ileride token'dan dolduracağız
  @Column(length = 64)
  private String authorId;

  @Column(length = 200)
  private String authorUsername;

  @Column(nullable = false)
  private Integer spentMinutes;

  @Column(nullable = false)
  private LocalDateTime workDate;

  @Column(nullable = false, length = 2000)
  private String description;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  public void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
