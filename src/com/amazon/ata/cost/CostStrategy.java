package com.amazon.ata.cost;

import com.amazon.ata.types.ShipmentOption;

public interface CostStrategy {
    /**
     * Returns the cost of the given shipment option, according to this particular cost strategy.
     * @param shipmentOption the shipment option to cost
     * @return the cost of the shipment option
     */
    Currency getCost(ShipmentOption shipmentOption);
}
