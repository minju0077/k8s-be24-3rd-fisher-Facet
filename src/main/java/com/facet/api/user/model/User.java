package com.facet.api.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("0")
    private Integer point;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 254)
    private String address;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;


    public void addPoint(Integer amount) {
        if(this.point == null){
            this.point = 0;
        }
        this.point += amount;
    }
}
