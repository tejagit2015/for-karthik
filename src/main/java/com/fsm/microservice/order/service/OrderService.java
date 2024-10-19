package com.fsm.microservice.order.service;

import com.fsm.microservice.order.client.InventoryClient;
import com.fsm.microservice.order.dto.OrderRequest;
import com.fsm.microservice.order.model.Order;
import com.fsm.microservice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest)
    {

        var isProductInStock = inventoryClient.isInStock(
                orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock)
        {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            order.setSkuCode(orderRequest.skuCode());
            orderRepository.save(order);
        }
        else {
            throw new RuntimeException("No Product Available");
        }

    }
}
