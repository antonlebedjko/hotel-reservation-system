package com.hotel.service.input;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomInput {
    private Integer roomNumber;
    private BigDecimal roomPricePerDay;
}
