package com.amazon.ata.types;

import com.amazon.ata.cost.Currency;

import java.util.Objects;

public class ShipmentCost implements Comparable<ShipmentCost> {
    private final ShipmentOption shipmentOption;
    private final Currency cost;

    public ShipmentCost(ShipmentOption shipmentOption, Currency cost) {
        this.shipmentOption = shipmentOption;
        this.cost = cost;
    }

    public ShipmentOption getShipmentOption() {
        return shipmentOption;
    }

    public Currency getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShipmentCost that = (ShipmentCost) o;
        return Objects.equals(shipmentOption, that.shipmentOption) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipmentOption, cost);
    }

    @Override
    public int compareTo(ShipmentCost other) {
        return this.cost.getAmount().compareTo(other.cost.getAmount());
    }
}
