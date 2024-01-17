package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.PriceSnapshotEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceSnapshot {
    private long userId;
    private String symbol;
    private double price;

    public PriceSnapshot(PriceSnapshotEntity priceSnapshotEntity) {
        userId = priceSnapshotEntity.getUserId();
        symbol = priceSnapshotEntity.getSymbol();
        price = priceSnapshotEntity.getPrice();
    }
}
