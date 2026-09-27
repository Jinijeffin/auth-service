package com.jini.auth_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=100)
    private String email;
    @Column(name = "hash_password", nullable=false)
    private String hashPassword;
    @Column(nullable=false)
    private boolean enabled=true;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    private Set<String> roles = new HashSet<>();
    @Column(name="refresh_token")
    private String refreshToken;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate(){

        this.createdAt=LocalDateTime.now();
    }

}
