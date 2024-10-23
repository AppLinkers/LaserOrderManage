package com.laser.ordermanage.factory.dto.request;

public class FactoryUpdateOrderIsUrgentRequestBuilder {
    public static FactoryUpdateOrderIsUrgentRequest isUrgentTrueBuild() {
        return new FactoryUpdateOrderIsUrgentRequest(Boolean.TRUE);
    }

    public static FactoryUpdateOrderIsUrgentRequest isUrgentFalseBuild() {
        return new FactoryUpdateOrderIsUrgentRequest(Boolean.FALSE);
    }

    public static FactoryUpdateOrderIsUrgentRequest nullIsUrgentBuild() {
        return new FactoryUpdateOrderIsUrgentRequest(null);
    }
}
