package org.example.service;

import org.example.dto.PriceSnapshot;
import org.example.entity.PriceSnapshotEntity;
import org.example.repository.PriceSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PriceSnapshotService {
    @Autowired
    private PriceSnapshotRepository priceSnapshotRepository;

    public PriceSnapshotEntity save(PriceSnapshot priceSnapshot) {
        return priceSnapshotRepository.save(new PriceSnapshotEntity(priceSnapshot));
    }

    public PriceSnapshotEntity save(PriceSnapshotEntity entity) {
        return priceSnapshotRepository.save(entity);
    }

    public Map<String, PriceSnapshotEntity> getAllByUserId(long userId) {
        return priceSnapshotRepository.findAllByUserId(userId).stream()
                .collect(Collectors.toMap(PriceSnapshotEntity::getSymbol, Function.identity()));
    }

    public List<PriceSnapshot> getAll() {
        return priceSnapshotRepository.findAll().stream()
                .map(PriceSnapshotEntity::createDTO)
                .collect(Collectors.toList());
    }
}

