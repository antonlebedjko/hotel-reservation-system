package com.hotel.repository;

import com.hotel.model.db.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.roomId NOT IN (SELECT res.room.roomId FROM Reservation res WHERE (res.checkInDate <= :checkOutDate AND res.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);
}
