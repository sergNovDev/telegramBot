package org.sergn.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.sergn.entity.enums.UserState;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId; //id пользователя в телеграмме
    @CreationTimestamp
    private LocalDateTime firstLoginDate; //дата подключения к нашему боту
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private UserState state;
}
