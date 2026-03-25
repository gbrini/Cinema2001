package com.cinema.model;

public class TicketType {
    private final int typeId;
    private final String  typeName;
    private final double baseDiscountPercent;
    private final double basepriceAddendum;
    
    public TicketType(int typeId, String typeName, double baseDiscountPercent, double basepriceAddendum) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.baseDiscountPercent = baseDiscountPercent;
        this.basepriceAddendum = basepriceAddendum;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public double getBaseDiscountPercent() {
        return baseDiscountPercent;
    }

    public double getBasepriceAddendum() {
        return basepriceAddendum;
    }
}
