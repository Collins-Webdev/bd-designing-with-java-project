
// MT5CarbonIntrospectionTests.java
package tct;

import com.amazon.ata.cost.CarbonCostStrategy;
import com.amazon.ata.cost.Currency;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("MT05_CARBON")
public class MT5CarbonIntrospectionTests {

    private static final BigDecimal CARBON_COST_PER_GRAM_CORRUGATE = new BigDecimal("0.017");
    private static final BigDecimal CARBON_COST_PER_GRAM_LAMINATED_PLASTIC = new BigDecimal("0.012");

    @Test
    void mt5_carbonCostStrategy_getCostOfBox_resultsInCorrectCarbonCost() {
        // GIVEN
        Box box = new Box(Material.CORRUGATE, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        ShipmentOption shipmentOption = ShipmentOption.builder()
                .withPackaging(box)
                .build();
        CarbonCostStrategy carbonCostStrategy = new CarbonCostStrategy();

        // WHEN
        Currency result = carbonCostStrategy.getCost(shipmentOption);

        // THEN
        BigDecimal expectedCost = box.getMass().multiply(CARBON_COST_PER_GRAM_CORRUGATE);
        assertEquals(expectedCost, result.getAmount(), "Carbon cost for box incorrect");
    }

    @Test
    void mt5_carbonCostStrategy_getCostOfPolyBag_resultsInCorrectCarbonCost() {
        // GIVEN
        PolyBag polyBag = new PolyBag(BigDecimal.valueOf(1000));
        ShipmentOption shipmentOption = ShipmentOption.builder()
                .withPackaging(polyBag)
                .build();
        CarbonCostStrategy carbonCostStrategy = new CarbonCostStrategy();

        // WHEN
        Currency result = carbonCostStrategy.getCost(shipmentOption);

        // THEN
        BigDecimal expectedCost = polyBag.getMass().multiply(CARBON_COST_PER_GRAM_LAMINATED_PLASTIC);
        assertEquals(expectedCost, result.getAmount(), "Carbon cost for poly bag incorrect");
    }
}
