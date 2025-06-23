package com.humanbooster.buisinessCase.model;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefresh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Refresh token must not be blank")
    @Column(name = "refresh_token")
    private String refreshToken;

    @NotNull(message = "User ID must not be null")
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @NotNull(message = "Issued At must not be null")
    @Column(name = "issued_at")
    private Date issuedAt;
}
