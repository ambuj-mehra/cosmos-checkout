package com.cosmos.service;

import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.dto.OmsResponse;

/**
 * The interface Ioms service.
 *
 * @author ambujmehra
 */
public interface IomsService {

    /**
     * Update order status oms response.
     *
     * @return the oms response
     */
    OmsResponse updateOrderStatus(OmsRequest omsRequest);
}
