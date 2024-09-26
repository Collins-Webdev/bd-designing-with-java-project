package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonetaryCostStrategyTest {

    private static final BigDecimal LABOR_COST = BigDecimal.valueOf(0.43);

    private MonetaryCostStrategy strategy;

    @BeforeEach
    public void setup() {
        strategy = new MonetaryCostStrategy();
    }

    @Test
    public void getCost_polybagShipmentOption_calculatesCorrectCost() {
        // GIVEN
        PolyBag polyBag = new PolyBag(BigDecimal.valueOf(1000));
        ShipmentOption shipmentOption = ShipmentOption.builder()
                .withPackaging(polyBag)
                .build();

        BigDecimal expectedMass = polyBag.getMass();
        BigDecimal materialCost = BigDecimal.valueOf(0.25);
        BigDecimal expectedCostAmount = expectedMass.multiply(materialCost).add(LABOR_COST);
        Currency expectedCost = new Currency(expectedCostAmount);

        // WHEN
        Currency cost = strategy.getCost(shipmentOption);

        // THEN
        assertEquals(expectedCost, cost,
                String.format("Expected cost of %s, but got %s", expectedCost, cost));
    }

    @Test
    public void getCost_boxShipmentOption_calculatesCorrectCost() {
        // GIVEN
        Box box = new Box(Material.CORRUGATE, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        ShipmentOption shipmentOption = ShipmentOption.builder()
                .withPackaging(box)
                .build();

        BigDecimal expectedMass = box.getMass();
        BigDecimal materialCost = BigDecimal.valueOf(0.005);
        BigDecimal expectedCostAmount = expectedMass.multiply(materialCost).add(LABOR_COST);
        Currency expectedCost = new Currency(expectedCostAmount);

        // WHEN
        Currency cost = strategy.getCost(shipmentOption);

        // THEN
        assertEquals(expectedCost, cost,
                String.format("Expected cost of %s, but got %s", expectedCost, cost));
    }
}