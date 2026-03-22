package com.facet.api.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String email;
    private String name;
    @Setter

    private String password;
    @Setter
    private boolean enable;

    @ColumnDefault(value="'ROLE_USER'")
    private String role;

    @Column(nullable = false)
    @Builder.Default
    private Integer point = 0;

    @Setter
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Setter
    @Column(length = 254)
    private String address;

    @Setter
    @Column(name = "birth_date")
    private LocalDate birthDate;


    public void addPoint(Integer amount) {
        if(this.point == null){
            this.point = 0;
        }
        this.point += amount;
    }
}
