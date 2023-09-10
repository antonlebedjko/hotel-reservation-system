package com.hotel.service;

import com.hotel.model.graphql.HotelAvailabilityStatistics;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.exception.RoomNotFoundException;
import com.hotel.model.db.Reservation;
import com.hotel.model.db.Room;
import com.hotel.service.input.RoomInput;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdministratorService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Iterable<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public Room findRoomById(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);

        if (room.isPresent()) {
            return room.get();
        }

        throw new RoomNotFoundException("Room with ID " + roomId + " not found");
    }

    public List<Reservation> roomSchedule(Long roomId) {
        return reservationRepository.findByRoomRoomId(roomId);
    }

    public HotelAvailabilityStatistics hotelAvailabilityStatistics(@Argument LocalDate from, @Argument LocalDate to) {
        List<Reservation> reservations = reservationRepository.findReservationsWithinDateRange(from, to);
        List<Room> allRooms = (List<Room>) roomRepository.findAll();

        int totalRooms = allRooms.size();
        int busyRooms = reservations.size();
        int freeRooms = totalRooms - busyRooms;

        List<Room> freeRoomsList = allRooms.stream()
                .filter(room -> reservations.stream().noneMatch(reservation -> reservation.getRoom().equals(room)))
                .collect(Collectors.toList());

        List<Room> busyRoomsList = allRooms.stream()
                .filter(room -> reservations.stream().anyMatch(reservation -> reservation.getRoom().equals(room)))
                .collect(Collectors.toList());

        return new HotelAvailabilityStatistics(from, to, totalRooms, freeRooms, busyRooms, freeRoomsList, busyRoomsList);
    }

    public Room addRoom(RoomInput room) {
        Room newRoom = new Room(null, room.getRoomNumber(), room.getRoomPricePerDay());
        return roomRepository.save(newRoom);
    }

    public Room editRoom(Long roomId, RoomInput room) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);

        if (optionalRoom.isPresent()) {
            Room existingRoom = optionalRoom.get();

            if (room.getRoomNumber() != null) {
                existingRoom.setRoomNumber(room.getRoomNumber());
            }
            if (room.getRoomPricePerDay() != null) {
                existingRoom.setRoomPricePerDay(room.getRoomPricePerDay());
            }

            return roomRepository.save(existingRoom);
        }

        throw new RoomNotFoundException("Room with ID " + roomId + " not found");
    }

    @Transactional
    public boolean deleteRoom(Long roomId) {
        if (roomRepository.existsById(roomId)) {
            reservationRepository.deleteByRoomRoomId(roomId);
            roomRepository.deleteById(roomId);
            return true;
        }
        return false;
    }
}
