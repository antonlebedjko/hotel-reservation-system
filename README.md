# **Hotel Reservation System**
<br/>

## About this project

This demo project represents an example of a hotel reservation system developed using Spring Boot, GraphQL, and Java 17.

It provides a streamlined GraphQL API designed to accommodate two user roles, each with specific request capabilities.

**Hotel administrators:**
- Manage rooms – view / add / remove / edit
- See schedule for each room
- See hotel availability statistics (how many rooms are free/busy) for specified period

**Customers:**
- Check availability on specified period
- Make a reservation
<br/>

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java 17:** You must have Java 17 installed on your system to run this project.
<br/>

## Examples of a GraphQL requests

**Hotel administrators:**

Add a new room
```graphql
mutation {
  addRoom(room: {roomNumber: 108, roomPricePerDay: 300.0}) {
    roomId,
    roomNumber,
    roomPricePerDay
  }
}
```

View rooms

```graphql

query {
  rooms {
    roomId,
    roomNumber,
    roomPricePerDay
  }
}
```

View a single room

```graphql

query {
  roomById (roomId: 2) {
    roomId
    roomNumber
    roomPricePerDay
    }
}
```

Edit existing room

```graphql

mutation {
  editRoom(roomId: 2, room: { roomNumber: 102, roomPricePerDay: 400 }) {
    roomId
    roomNumber
    roomPricePerDay
  }
}
```

Delete a room

```graphql

mutation {
  deleteRoom(roomId: 2)
}
```

See schedule for each room

```graphql
query {
  roomSchedule(roomId: 1) {
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
```

See hotel availability statistics

```graphql
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
```

**Customers:**

Check availability on a specified period

```graphql
query {
  checkAllAvailableRooms(checkInDate: "2023-09-10", checkOutDate: "2023-09-17") {
      roomId
      roomNumber
      roomPricePerDay
  }
}
```

Make a reservation

```graphql

mutation {
  makeReservation(input: {
    roomId: 1,
    checkInDate: "2021-12-12",
    checkOutDate: "2022-12-12"
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
```




