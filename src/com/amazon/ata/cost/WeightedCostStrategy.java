package com.amazon.ata.cost;

import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WeightedCostStrategy implements CostStrategy {
    private static final BigDecimal MONETARY_WEIGHT = new BigDecimal("0.8");
    private static final BigDecimal CARBON_WEIGHT = new BigDecimal("0.2");

    private final MonetaryCostStrategy monetaryCostStrategy;
    private final CarbonCostStrategy carbonCostStrategy;

    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        this.monetaryCostStrategy = monetaryCostStrategy;
        this.carbonCostStrategy = carbonCostStrategy;
    }

    @Override
    public Currency getCost(ShipmentOption shipmentOption) {
        BigDecimal monetaryCost = monetaryCostStrategy.getCost(shipmentOption).getAmount();
        BigDecimal carbonCost = carbonCostStrategy.getCost(shipmentOption).getAmount();

        BigDecimal weightedCost = monetaryCost.multiply(MONETARY_WEIGHT)
                .add(carbonCost.multiply(CARBON_WEIGHT))
                .setScale(2, RoundingMode.HALF_UP);

        return new Currency(weightedCost);
    }
}