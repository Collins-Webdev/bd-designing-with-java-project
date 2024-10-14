package com.amazon.ata.cost;

import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.service.ShipmentService;

public class App {
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
