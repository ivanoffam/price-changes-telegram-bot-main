package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.PriceSnapshot;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PriceSnapshotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String symbol;
    private double price;

    public PriceSnapshotEntity(PriceSnapshot priceSnapshot) {
        userId = priceSnapshot.getUserId();
        symbol = priceSnapshot.getSymbol();
        price = priceSnapshot.getPrice();
    }

    public PriceSnapshot createDTO() {
        return new PriceSnapshot(this);
    }
}

