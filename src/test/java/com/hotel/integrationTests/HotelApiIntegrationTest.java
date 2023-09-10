package com.hotel.integrationTests;

import com.hotel.model.db.Customer;
import com.hotel.model.db.Reservation;
import com.hotel.model.db.Room;
import com.hotel.model.graphql.HotelAvailabilityStatistics;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.ReservationRepository;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:/application.properties")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelApiIntegrationTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Order(1)
    void addRoomShouldAddRoom() {
        String document1 = """
            mutation {
              addRoom(room: {roomNumber: 108, roomPricePerDay: 300}) {
                roomId,
                roomNumber,
                roomPricePerDay
              }
            }
        """;

        graphQlTester.document(document1)
                .execute()
                .path("addRoom")
                .entity(Room.class)
                .satisfies(room -> {
                    assertNotNull(room.getRoomId());
                    assertEquals(108, room.getRoomNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(300), room.getRoomPricePerDay());
                });

        String document2 = """
            mutation {
              addRoom(room: {roomNumber: 109, roomPricePerDay: 220}) {
                roomId,
                roomNumber,
                roomPricePerDay
              }
            }
        """;

        graphQlTester.document(document2)
                .execute()
                .path("addRoom")
                .entity(Room.class)
                .satisfies(room -> {
                    assertNotNull(room.getRoomId());
                    assertEquals(109, room.getRoomNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(220), room.getRoomPricePerDay());
                });
    }

    @Test
    @Order(2)
    void roomsShouldReturnAllRooms() {
        String document = """
             query{
                  rooms {
                    roomId,
                    roomNumber,
                    roomPricePerDay
                  }
             }
        """;

       graphQlTester.document(document)
                .execute()
                .path("rooms")
                .entityList(Room.class)
                .hasSize(2);

    }

    @Test
    @Order(3)
    void editRoomShouldUpdateExistingRoom() {
        String document = """
            mutation {
                editRoom(roomId: 2, room: { roomNumber: 109, roomPricePerDay: 215 }) {
                    roomId
                    roomNumber
                    roomPricePerDay
                }
            }
        """;

        graphQlTester.document(document)
                .execute()
                .path("editRoom")
                .entity(Room.class)
                .satisfies(room -> {
                    assertNotNull(room.getRoomId());
                    assertEquals(109, room.getRoomNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(215), room.getRoomPricePerDay());
                });
    }

    @Test
    @Order(4)
    void roomByIdShouldReturnUpdatedRoom() {
        String document = """
             query {
                    roomById (roomId: 2) {
                       roomId
                       roomNumber
                       roomPricePerDay
                    }
             }
        """;

        graphQlTester.document(document)
                .execute()
                .path("roomById")
                .entity(Room.class)
                .satisfies(room -> {
                    assertNotNull(room.getRoomId());
                    assertEquals(109, room.getRoomNumber());
                    Assertions.assertEquals(BigDecimal.valueOf(215.0), room.getRoomPricePerDay());
                });
    }

    @Test
    @Order(5)
    void roomScheduleShouldReturnAllReservationsForARoom() {
        // Fetch a real room
        String roomDocument = """
             query {
                    roomById (roomId: 2) {
                       roomId
                       roomNumber
                       roomPricePerDay
                    }
             }
        """;

        Room room = graphQlTester.document(roomDocument)
                .execute()
                .path("roomById")
                .entity(Room.class)
                .get();
        // Create a customer and some reservations
        Customer customer = new Customer(null, "email1@gmail.com", "+371 266666666", "John", "Smith");
        customerRepository.save(customer);
        Reservation reservation1 = new Reservation(null, LocalDate.of(2023, 9, 15), LocalDate.of(2023, 9, 17), customer, room);
        Reservation reservation2 = new Reservation(null, LocalDate.of(2023, 10, 22), LocalDate.of(2023, 10, 26), customer, room);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        String roomScheduleDocument = """
             query {
                  roomSchedule(roomId: 2) {
                        reservationId
                        checkInDate
                        checkOutDate
                        customer {
                          customerId
                          firstName
                          lastName
                        }
                        room {
                          roomId
                          roomNumber
                          roomPricePerDay
                        }
                  }
             }
        """;

        List<Reservation> schedules = graphQlTester.document(roomScheduleDocument)
                .execute()
                .path("roomSchedule")
                .entityList(Reservation.class)
                .get();

        assertEquals(2, schedules.size());
        assertEquals(109, schedules.get(0).getRoom().getRoomNumber());
        assertEquals(109, schedules.get(1).getRoom().getRoomNumber());
        assertEquals("John", schedules.get(0).getCustomer().getFirstName());
        assertEquals("John", schedules.get(1).getCustomer().getFirstName());
        assertEquals("Smith", schedules.get(0).getCustomer().getLastName());
        assertEquals("Smith", schedules.get(1).getCustomer().getLastName());
    }

    @Test
    @Order(6)
    void hotelAvailabilityStatisticsShouldReturnAllStats(){
        String document = """
                     query {
                          hotelAvailabilityStatistics(from: "2023-09-15", to: "2023-09-20") {
                                from
                                to
                                totalRooms
                                freeRooms
                                busyRooms
                                freeRoomsList {
                                    roomId
                                    roomNumber
                                    roomPricePerDay
                                }
                                busyRoomsList {
                                    roomId
                                    roomNumber
                                    roomPricePerDay
                                }
                          }
                     }
                """;

        HotelAvailabilityStatistics statistics = graphQlTester.document(document)
                .execute()
                .path("hotelAvailabilityStatistics")
                .entity(HotelAvailabilityStatistics.class)
                .get();

        assertEquals(2, statistics.getTotalRooms());
        assertEquals(1, statistics.getBusyRooms());
        assertEquals(1, statistics.getFreeRooms());
        assertEquals(1, statistics.getBusyRoomsList().size());
        assertEquals(1, statistics.getFreeRoomsList().size());
        Assert.assertEquals(108, statistics.getFreeRoomsList().get(0).getRoomNumber());
        Assert.assertEquals(109, statistics.getBusyRoomsList().get(0).getRoomNumber());

    }

    @Test
    @Order(7)
    void checkAllAvailableRoomsShouldReturnListOfRooms() {
        String document = """
                     query {
                          checkAllAvailableRooms(checkInDate: "2023-09-16", checkOutDate: "2023-09-22") {
                                      roomId
                                      roomNumber
                                      roomPricePerDay
                                	}
                     }
                """;

        graphQlTester.document(document)
                .execute()
                .path("checkAllAvailableRooms")
                .entityList(Room.class)
                .hasSize(1);
    }

    @Test
    @Order(8)
    void makeReservationShouldBookARoom() {
        String document = """
            mutation {
                makeReservation(input: {
                    roomId: 2,
                    checkInDate: "2024-09-18",
                    checkOutDate: "2024-09-26"
                    customerId:1,
                  }) {
                        reservationId
                        checkInDate
                        checkOutDate
                        customer {
                          customerId
                          firstName
                          lastName
                        }
                        room {
                          roomId
                          roomNumber
                          roomPricePerDay
                        }
                 }
            }
        """;

        graphQlTester.document(document)
                .execute()
                .path("makeReservation")
                .entity(Reservation.class)
                .satisfies(reservation -> {
                    assertNotNull(reservation.getReservationId());
                    assertEquals("John",reservation.getCustomer().getFirstName());
                    Assertions.assertEquals(2, reservation.getRoom().getRoomId());
                    Assertions.assertEquals(109, reservation.getRoom().getRoomNumber());
                });

    }

    @Test
    @Order(9)
    void deleteRoomShouldRemoveRoomFromDb(){
        String documentDeletion1 = """
                mutation{
                    deleteRoom(roomId: 1)
                }   
              """;

        graphQlTester.document(documentDeletion1)
                .execute()
                .path("deleteRoom")
                .entity(Boolean.class)
                .satisfies(result -> {
                    assertEquals(true, result);
                });

        String documentDeletion2 = """
                mutation{
                    deleteRoom(roomId: 2)
                }   
              """;

        graphQlTester.document(documentDeletion2)
                .execute()
                .path("deleteRoom")
                .entity(Boolean.class)
                .satisfies(result -> {
                    assertEquals(true, result);
                });
    }

    @Test
    @Order(10)
    void roomsShouldReturnEmptyListAfterDeletion() {
        String document = """
             query{
                  rooms {
                    roomId,
                    roomNumber,
                    roomPricePerDay
                  }
             }
        """;

        graphQlTester.document(document)
                .execute()
                .path("rooms")
                .entityList(Room.class)
                .hasSize(0);

    }

}
