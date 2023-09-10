package com.hotel.service;

import com.hotel.exception.CustomerNotFoundException;
import com.hotel.exception.RoomIsNotAvailableOnThatDateException;
import com.hotel.exception.RoomNotFoundException;
import com.hotel.model.db.Reservation;
import com.hotel.model.db.Room;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.service.input.ReservationInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Reservation makeReservation(ReservationInput reservationInput) {
        LocalDate checkInDate = reservationInput.getCheckInDate();
        LocalDate checkOutDate = reservationInput.getCheckOutDate();
        Long roomId = reservationInput.getRoomId();
        Long customerId = reservationInput.getCustomerId();

        // Validate that checkInDate is before checkOutDate
        if (checkInDate.isEqual(checkOutDate) || checkInDate.isAfter(checkOutDate)) {
            throw new IllegalArgumentException("Check-in date must be before the check-out date.");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new));
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);

        // Check if the room is available for the specified date range
        List<Room> availableRooms = checkAllAvailableRooms(checkInDate, checkOutDate);

        // Find the room by ID
        Room selectedRoom = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        // Check if the selected room is in the list of available rooms
        if (!availableRooms.contains(selectedRoom)) {
            throw new RoomIsNotAvailableOnThatDateException("The selected room is not available for the specified date range.");
        }

        reservation.setRoom(selectedRoom);

        return reservationRepository.save(reservation);
    }

    public List<Room> checkAllAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate that checkInDate is before checkOutDate
        if (checkInDate.isEqual(checkOutDate) || checkInDate.isAfter(checkOutDate)) {
            throw new IllegalArgumentException("Check-in date must be before the check-out date.");
        }
        return roomRepository.findAvailableRooms(checkInDate, checkOutDate);
    }
}
