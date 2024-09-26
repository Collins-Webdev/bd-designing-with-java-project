package com.amazon.ata.service;

import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShipmentService {
    public ShipmentService(PackagingDAO packagingDAO, MonetaryCostStrategy monetaryCostStrategy) {
    }

    // ... other code ...

    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        List<ShipmentOption> shipmentOptions = PackagingDAO.findShipmentOptions(item, fulfillmentCenter);

        if (shipmentOptions.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s", fulfillmentCenter.getFcCode(), item)
            );
        }

        return shipmentOptions;
    }

    public ShipmentCost findLowestCostShipmentOption(List<ShipmentOption> shipmentOptions, CostStrategy costStrategy) {
        List<ShipmentCost> shipmentCosts = new ArrayList<>();
        for (ShipmentOption option : shipmentOptions) {
            shipmentCosts.add(new ShipmentCost(option, costStrategy.getCost(option)));
        }
        return Collections.min(shipmentCosts);
    }

    public ShipmentOption findShipmentOption(Item smallItem, FulfillmentCenter existentFC) {
        return null;
    }
}