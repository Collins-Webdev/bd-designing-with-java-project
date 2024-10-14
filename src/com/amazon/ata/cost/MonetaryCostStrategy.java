package com.amazon.ata.cost;

import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.service.ShipmentService;
import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Considers both material cost and labor cost to calculate the total cost of a ShipmentOption.
 */

public class MonetaryCostStrategy implements CostStrategy {
    private static final BigDecimal LABOR_COST = BigDecimal.valueOf(0.43);
    private final Map<Material, BigDecimal> materialCostPerGram;

    /**
     * Initializes a MonetaryCostStrategy.
     */
    public MonetaryCostStrategy() {
        materialCostPerGram = new HashMap<>();
        materialCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(.005));
        materialCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(.25));
    }

    @Override
    public Currency getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal materialCost = this.materialCostPerGram.get(packaging.getMaterial());

        BigDecimal cost = packaging.getMass().multiply(materialCost).add(LABOR_COST);
        return new Currency(cost);
    }

    /**
     * Calculates the carbon cost of a ShipmentOption based on the packaging material and mass.
     */
    public static class CarbonCostStrategy implements CostStrategy {
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

    public static class WeightedCostStrategy implements CostStrategy {
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

    public static class App {
        /* don't instantiate me */
        private App() {}

        private static PackagingDatastore getPackagingDatastore() {
            return new PackagingDatastore();
        }

        private static PackagingDAO getPackagingDAO() {
            return new PackagingDAO(getPackagingDatastore());
        }

        private static CostStrategy getCostStrategy() {
            MonetaryCostStrategy monetaryCostStrategy = new MonetaryCostStrategy();
            CarbonCostStrategy carbonCostStrategy = new CarbonCostStrategy();
            return new WeightedCostStrategy(monetaryCostStrategy, carbonCostStrategy);
        }

        public static ShipmentService getShipmentService() {
            return new ShipmentService(getPackagingDAO(), (MonetaryCostStrategy) getCostStrategy());
        }
    }
}
