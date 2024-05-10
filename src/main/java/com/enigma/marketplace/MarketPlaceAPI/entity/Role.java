package com.enigma.marketplace.MarketPlaceAPI.entity;

import com.enigma.marketplace.MarketPlaceAPI.constant.TableName;
import com.enigma.marketplace.MarketPlaceAPI.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.ROLE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}

