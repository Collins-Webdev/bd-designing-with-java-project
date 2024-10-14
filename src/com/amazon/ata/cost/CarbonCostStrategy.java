package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the carbon cost of a ShipmentOption based on the packaging material and mass.
 */
public class CarbonCostStrategy implements CostStrategy {
    private static final BigDecimal CARBON_UNITS_PER_GRAM_CORRUGATE = new BigDecimal("0.017");
    private static final BigDecimal CARBON_UNITS_PER_GRAM_LAMINATED_PLASTIC = new BigDecimal("0.012");

    private final Map<Material, BigDecimal> materialCarbonCostPerGram;

    /**
     * Initializes a CarbonCostStrategy.
     */
    public CarbonCostStrategy() {
        materialCarbonCostPerGram = new HashMap<>();
        materialCarbonCostPerGram.put(Material.CORRUGATE, CARBON_UNITS_PER_GRAM_CORRUGATE);
        materialCarbonCostPerGram.put(Material.LAMINATED_PLASTIC, CARBON_UNITS_PER_GRAM_LAMINATED_PLASTIC);
    }

    @Override
    public Currency getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal carbonCostPerGram = this.materialCarbonCostPerGram.get(packaging.getMaterial());

        BigDecimal carbonCost = packaging.getMass().multiply(carbonCostPerGram);
        return new Currency(carbonCost);
    }
}
