package com.hotel;

import com.hotel.model.db.Reservation;
import com.hotel.model.db.Customer;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.model.db.Room;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	Environment environment;

	@PostConstruct
	public void populateData() {

		if (!Arrays.asList(environment.getActiveProfiles()).contains("main")) {
			return;
		}

		Customer customer1 = new Customer(null, "customer1@inbox.lv", "+371 2222111", "Antons", "Lebedjko");
		Customer customer2 = new Customer(null, "customer1@inbox.lv", "+371 2666643", "Kristaps", "Porziņģis");

		customerRepository.save(customer1);
		customerRepository.save(customer2);

		Room room1 = new Room(null, 101, 100.00);
		Room room2 = new Room(null, 102, 120.00);
		Room room3 = new Room(null, 103, 90.50);

		roomRepository.save(room1);
		roomRepository.save(room2);
		roomRepository.save(room3);

		Reservation reservation1 = new Reservation(null, LocalDate.of(2023, 9, 15), LocalDate.of(2023, 9, 17), customer1, room1);
		Reservation reservation2 = new Reservation(null, LocalDate.of(2023, 9, 16), LocalDate.of(2023, 9, 24), customer2, room3);
		reservationRepository.save(reservation1);
		reservationRepository.save(reservation2);
	}

}
