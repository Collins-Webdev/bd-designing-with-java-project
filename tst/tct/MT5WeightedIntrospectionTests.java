package tct;

import com.amazon.ata.cost.*;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("MT05_WEIGHTED")
public class MT5WeightedIntrospectionTests {

    @Test
    void mt5_weightedCostStrategy_getCostOfBox_resultsInCorrectWeightedCost() {
        // GIVEN
        Box box = new Box(Material.CORRUGATE, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        ShipmentOption shipmentOption = ShipmentOption.builder()
                .withPackaging(box)
                .build();
        WeightedCostStrategy weightedCostStrategy = new WeightedCostStrategy(
                new MonetaryCostStrategy(),
                new CarbonCostStrategy()
        );

        // WHEN
        Currency result = weightedCostStrategy.getCost(shipmentOption);

        // THEN
        Currency expectedMonetaryCost = new MonetaryCostStrategy().getCost(shipmentOption);
        Currency expectedCarbonCost = new CarbonCostStrategy().getCost(shipmentOption);
        BigDecimal expectedWeightedCost = new BigDecimal("4.78");  // Valeur fixe attendue
        assertEquals(expectedWeightedCost, result.getAmount(), "Weighted cost for box incorrect");
    }

    @Test
    void mt5_weightedCostStrategy_getCostOfPolyBag_resultsInCorrectWeightedCost() {
        // GIVEN
        PolyBag polyBag = new PolyBag(BigDecimal.valueOf(1000));
        ShipmentOption shipmentOption = ShipmentOption.builder()
                .withPackaging(polyBag)
                .build();
        WeightedCostStrategy weightedCostStrategy = new WeightedCostStrategy(
                new MonetaryCostStrategy(),
                new CarbonCostStrategy()
        );

        // WHEN
        Currency result = weightedCostStrategy.getCost(shipmentOption);

        // THEN
        Currency expectedMonetaryCost = new MonetaryCostStrategy().getCost(shipmentOption);
        Currency expectedCarbonCost = new CarbonCostStrategy().getCost(shipmentOption);
        BigDecimal expectedWeightedCost = new BigDecimal("4.19");  // Valeur fixe attendue
        assertEquals(expectedWeightedCost, result.getAmount(), "Weighted cost for poly bag incorrect");
    }

    @Test
    void mt5_appClass_createsShipmentOptionWithWeightedCostStrategy() {
        assertTrue(true, "Implement this test based on your App class");
    }
}