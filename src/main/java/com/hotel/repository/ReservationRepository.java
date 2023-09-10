package com.hotel.repository;

import com.hotel.model.db.Reservation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findByRoomRoomId(Long roomId);

    @Query("SELECT r FROM Reservation r WHERE r.checkInDate >= :from AND r.checkOutDate <= :to")
    List<Reservation> findReservationsWithinDateRange(LocalDate from, LocalDate to);

    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.room.roomId = :roomId")
    void deleteByRoomRoomId(Long roomId);

}
