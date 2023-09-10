package com.hotel.service.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationInput {
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long customerId;
}
