package com.cinema.model;

public class TicketType {
    private final int typeId;
    private final String typeName;
    private final double baseDiscountPercent;
    private final double basePriceAddendum;

    public TicketType(int typeId, String typeName, double baseDiscountPercent, double basePriceAddendum) {
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

    public double getBaseDiscountPercent() {
        return baseDiscountPercent;
    }

    public double getBasePriceAddendum() {
        return basePriceAddendum;
    }
}
