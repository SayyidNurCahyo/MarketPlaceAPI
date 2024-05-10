package com.enigma.marketplace.MarketPlaceAPI.entity;

import com.enigma.marketplace.MarketPlaceAPI.constant.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.TRANSACTION_DETAIL)
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @Column(name = "quantity")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @JsonBackReference
    private Transaction transaction;
}
