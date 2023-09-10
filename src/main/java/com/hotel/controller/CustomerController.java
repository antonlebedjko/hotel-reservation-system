package com.hotel.controller;

import com.hotel.model.db.Reservation;
import com.hotel.model.db.Room;
import com.hotel.service.CustomerService;
import com.hotel.service.input.ReservationInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @MutationMapping
    Reservation makeReservation(@Argument ReservationInput input) {
        return customerService.makeReservation(input);
    }

    @QueryMapping
    List<Room> checkAllAvailableRooms(@Argument LocalDate checkInDate, @Argument LocalDate checkOutDate) {
        return customerService.checkAllAvailableRooms(checkInDate, checkOutDate);
    }
}
