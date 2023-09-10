package com.hotel.controller;

import com.hotel.model.db.Reservation;
import com.hotel.model.db.Room;
import com.hotel.model.graphql.HotelAvailabilityStatistics;
import com.hotel.service.AdministratorService;
import com.hotel.service.input.RoomInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @QueryMapping
    Iterable<Room> rooms() {
        return administratorService.findAllRooms();
    }

    @QueryMapping
    Room roomById(@Argument Long roomId) {
        return administratorService.findRoomById(roomId);
    }

    @QueryMapping
    List<Reservation> roomSchedule(@Argument Long roomId) {
        return administratorService.roomSchedule(roomId);
    }

    @QueryMapping
    HotelAvailabilityStatistics hotelAvailabilityStatistics(@Argument LocalDate from, @Argument LocalDate to) {
        return administratorService.hotelAvailabilityStatistics(from, to);
    }

    @MutationMapping
    Room addRoom(@Argument RoomInput room) {
        return administratorService.addRoom(room);
    }

    @MutationMapping
    Room editRoom(@Argument Long roomId, @Argument RoomInput room) {
        return administratorService.editRoom(roomId, room);
    }

    @MutationMapping
    boolean deleteRoom(@Argument Long roomId) {
        return administratorService.deleteRoom(roomId);
    }

}
