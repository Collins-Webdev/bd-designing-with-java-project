package com.amazon.ata.datastore;

import com.amazon.ata.types.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackagingDatastore {
    private final List<FcPackagingOption> fcPackagingOptions;

    public PackagingDatastore() {
        fcPackagingOptions = new ArrayList<>(Arrays.asList(
                createFcPackagingOption("IND1", Material.CORRUGATE, "10", "10", "10"),
                createFcPackagingOption("ABE2", Material.CORRUGATE, "20", "20", "20"),
                createFcPackagingOption("ABE2", Material.CORRUGATE, "40", "40", "40"),
                createFcPackagingOption("YOW4", Material.CORRUGATE, "10", "10", "10"),
                createFcPackagingOption("YOW4", Material.CORRUGATE, "20", "20", "20"),
                createFcPackagingOption("YOW4", Material.CORRUGATE, "60", "60", "60"),
                createFcPackagingOption("IAD2", Material.CORRUGATE, "20", "20", "20"),
                createFcPackagingOption("PDX1", Material.CORRUGATE, "40", "40", "40"),
                createFcPackagingOption("PDX1", Material.CORRUGATE, "60", "60", "60"),
                createFcPackagingOptionPolyBag("IAD2", "2000"),
                createFcPackagingOptionPolyBag("IAD2", "10000")
        ));

        // Add new PolyBag FcPackagingOptions
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("IAD2", "5000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("YOW4", "2000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("YOW4", "5000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("YOW4", "10000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("IND1", "2000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("IND1", "5000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("ABE2", "2000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("ABE2", "6000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("PDX1", "5000"));
        fcPackagingOptions.add(createFcPackagingOptionPolyBag("PDX1", "10000"));
    }

    private FcPackagingOption createFcPackagingOption(String fcCode, Material material,
                                                      String length, String width, String height) {
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter(fcCode);
        Box box = new Box(material, new BigDecimal(length), new BigDecimal(width), new BigDecimal(height));
        return new FcPackagingOption(fulfillmentCenter, box);
    }

    private FcPackagingOption createFcPackagingOptionPolyBag(String fcCode, String volume) {
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter(fcCode);
        PolyBag polyBag = new PolyBag(new BigDecimal(volume));
        return new FcPackagingOption(fulfillmentCenter, polyBag);
    }

    public List<FcPackagingOption> getFcPackagingOptions() {
        return fcPackagingOptions;
    }
}