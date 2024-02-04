package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Service.DateAndTimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateAndTimeServiceImpl implements DateAndTimeService {
    @Override
    public String extractDateFromOrderDate(String orderDate) {
        // Define the format pattern considering the space between date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");

        // Parse the orderDate string into LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(orderDate, formatter);

        // Format the date as "yyyy-MM-dd"
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
