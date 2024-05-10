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
@Table(name = TableName.MERCHANT)
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<Product> products;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
