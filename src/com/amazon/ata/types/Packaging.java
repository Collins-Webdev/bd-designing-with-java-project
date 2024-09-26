package com.amazon.ata.types;

import java.math.BigDecimal;

public abstract class Packaging {
    private Material material;

    public Packaging(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public abstract boolean canFitItem(Item item);
    public abstract BigDecimal getMass();
}