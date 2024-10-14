package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;

import java.util.*;

public class PackagingDAO {
    private static Map<FulfillmentCenter, Set<Packaging>> fcPackagingOptions;

    public PackagingDAO(PackagingDatastore datastore) {
        fcPackagingOptions = new HashMap<>();
        for (FcPackagingOption option : datastore.getFcPackagingOptions()) {
            FulfillmentCenter fc = option.getFulfillmentCenter();
            Packaging packaging = option.getPackaging();
            fcPackagingOptions.computeIfAbsent(fc, k -> new HashSet<>()).add(packaging);
        }
    }

    public static List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        Set<Packaging> packagingSet = fcPackagingOptions.get(fulfillmentCenter);
        if (packagingSet == null) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        List<ShipmentOption> result = new ArrayList<>();
        for (Packaging packaging : packagingSet) {
            if (packaging.canFitItem(item)) {
                result.add(ShipmentOption.builder()
                        .withItem(item)
                        .withPackaging(packaging)
                        .withFulfillmentCenter(fulfillmentCenter)
                        .build());
            }
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }
}