package com.rishi.authsystem.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Automatically set before insert
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
