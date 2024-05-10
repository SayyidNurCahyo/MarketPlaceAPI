package com.enigma.marketplace.MarketPlaceAPI.entity;

import com.enigma.marketplace.MarketPlaceAPI.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.REWARD)
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "reward_point")
    private Integer point;
}
