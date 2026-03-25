package com.cinema.model;

public class TicketType {
    private final int typeId;
    private final String typeName;
    private final float baseDiscountPercent;
    private final float basePriceAddendum;

    public TicketType(int typeId, String typeName, float baseDiscountPercent, float basePriceAddendum) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.baseDiscountPercent = baseDiscountPercent;
        this.basePriceAddendum = basePriceAddendum;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public float getBaseDiscountPercent() {
        return baseDiscountPercent;
    }

    public float getBasePriceAddendum() {
        return basePriceAddendum;
    }
}
