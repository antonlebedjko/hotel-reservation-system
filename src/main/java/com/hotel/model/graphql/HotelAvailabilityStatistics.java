package com.hotel.model.graphql;

import com.hotel.model.db.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelAvailabilityStatistics {
    private LocalDate from;
    private LocalDate to;
    private int totalRooms;
    private int freeRooms;
    private int busyRooms;
    private List<Room> freeRoomsList;
    private List<Room> busyRoomsList;
}
