package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.configuration.BotConfig;
import org.example.dto.PriceSnapshot;
import org.example.dto.PriceSnapshotDTO;
import org.example.dto.User;
import org.example.entity.PriceSnapshotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for checking cryptocurrency prices and notifying users about price changes.
 */
@Slf4j
@Service
public class PriceCheckerService {
    @Autowired
    private UserManager userManager;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PriceSnapshotService priceSnapshotService;
    @Autowired
    private BotConfig config;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Scheduled method to refresh cryptocurrency price snapshots and notify users about significant price changes.
     */
    @Scheduled(fixedDelayString = "${crypto.refreshIntervalSeconds}000")
    public void refreshPriceSnaphots() throws Exception {
        List<User> users = userManager.getUsers();
        if (users.isEmpty()) {
            return;
        }
        List<String> symbols = new ArrayList<>();
        List<PriceSnapshotDTO> currentPriceSnapshots = getPriceSnapshots();

        for (User user : users) {
            symbols.clear();
            Map<String, PriceSnapshotEntity> initialPriceBySymbol = priceSnapshotService.getAllByUserId(user.getUserId());

            currentPriceSnapshots.forEach(snapshot -> {
                String symbol = snapshot.getSymbol();
                double currentPrice = snapshot.getPrice();
                boolean isSymbolExists = initialPriceBySymbol.containsKey(symbol);

                if (!isSymbolExists) {
                    PriceSnapshot priceSnapshot = new PriceSnapshot(user.getUserId(), symbol, currentPrice);
                    priceSnapshotService.save(priceSnapshot);
                    return;
                }
                PriceSnapshotEntity entity = initialPriceBySymbol.get(symbol);

                if (isPriceChangeThresholdExceeded(entity.getPrice(), currentPrice)) {
                    symbols.add(symbol);
                    entity.setPrice(currentPrice);
                    priceSnapshotService.save(entity);
                }
            });
            if (symbols.isEmpty()) {
                continue;
            }
            String formattedSymbols = symbols.stream().collect(Collectors.joining(", "));
            notificationService.sendSimpleMessage(config.getChatId(), "Cryptocurrencies whose price has changed more than the threshold value: " + formattedSymbols);
        }
    }

    /**
     * Retrieves cryptocurrency price snapshots from the external API.
     *
     * @return List of PriceSnapshot objects representing cryptocurrency prices.
     */
    private List<PriceSnapshotDTO> getPriceSnapshots() throws Exception {
        String jsonResponse = restTemplate.getForObject(config.getApiMexcUrl(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        PriceSnapshotDTO[] response = objectMapper.readValue(jsonResponse, PriceSnapshotDTO[].class);

        return response != null ?
                Arrays.asList(response) : Collections.emptyList();
    }

    /**
     * Checks whether the price change between initial and current prices exceeds the configured threshold.
     *
     * @param initialPrice Initial cryptocurrency price.
     * @param currentPrice Current cryptocurrency price.
     * @return True if the price change exceeds the threshold, false otherwise.
     */
    private boolean isPriceChangeThresholdExceeded(double initialPrice, double currentPrice) {
        return initialPrice != 0
                && currentPrice != 0
                && Math.abs(currentPrice - initialPrice) / initialPrice * 100 >= config.getPriceChangeThreshold();
    }
}