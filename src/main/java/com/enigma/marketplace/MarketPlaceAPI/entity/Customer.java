package com.enigma.marketplace.MarketPlaceAPI.entity;

import com.enigma.marketplace.MarketPlaceAPI.constant.TableName;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.CUSTOMER)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "reward_point")
    private Integer point;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
