package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.Order;

import java.time.format.DateTimeFormatter;

public interface OrderScheduledService {

    void orderChangeSchedule();

    void changeStatus(Order order, DateTimeFormatter formatter);
}
