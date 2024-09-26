package com.amazon.ata.types;

import com.amazon.ata.cost.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ShipmentCostTest {

    private static final Currency COST = new Currency(BigDecimal.TEN);
    private static final Packaging PACKAGING = new Box(Material.CORRUGATE, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
    private static FulfillmentCenter FC = new FulfillmentCenter("IND123");
    private ShipmentOption SHIPMENT_OPTION = ShipmentOption.builder()
            .withFulfillmentCenter(FC)
            .withPackaging(PACKAGING)
            .build();

    private ShipmentCost shipmentCost;

    @BeforeEach
    public void setup() {
        shipmentCost = new ShipmentCost(SHIPMENT_OPTION, COST);
    }

    @Test
    public void equals_differentCost_returnsFalse() {
        // GIVEN
        Object other = new ShipmentCost(SHIPMENT_OPTION, new Currency(COST.getAmount().add(BigDecimal.ONE)));

        // WHEN
        boolean isEqual = shipmentCost.equals(other);

        // THEN
        assertFalse(isEqual, "ShipmentCosts with different costs are not equal.");
    }

    @Test
    public void compareTo_greaterCost_isNegative() {
        // GIVEN
        ShipmentCost shipmentCost = new ShipmentCost(SHIPMENT_OPTION, COST);
        ShipmentCost greaterShipmentCost = new ShipmentCost(SHIPMENT_OPTION, new Currency(COST.getAmount().add(BigDecimal.ONE)));

        // WHEN
        int result = shipmentCost.compareTo(greaterShipmentCost);

        // THEN
        assertTrue(result < 0, "Comparison to greater ShipmentCost should be negative");
    }

    @Test
    public void compareTo_lesserCost_isPositive() {
        // GIVEN
        ShipmentCost shipmentCost = new ShipmentCost(SHIPMENT_OPTION, COST);
        ShipmentCost lesserShipmentCost = new ShipmentCost(SHIPMENT_OPTION, new Currency(COST.getAmount().subtract(BigDecimal.ONE)));

        // WHEN
        int result = shipmentCost.compareTo(lesserShipmentCost);

        // THEN
        assertTrue(result > 0, "Comparison to lesser ShipmentCost should be positive");
    }

    // ... other tests remain the same ...
}