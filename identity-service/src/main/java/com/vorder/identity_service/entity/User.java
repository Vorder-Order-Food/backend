package com.vorder.identity_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //ko hien thi pass khi get profile
    private String password;
    private boolean isEnabled = false;

    @ManyToMany
    Set<Role> roles;

    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified;

    @ElementCollection
    private List<Long> favoriteRestaurantIds = new ArrayList<>();

    @ElementCollection
    private List<Long> orderIds = new ArrayList<>();

    @ElementCollection
    private List<Long> addressIds = new ArrayList<>();


}
