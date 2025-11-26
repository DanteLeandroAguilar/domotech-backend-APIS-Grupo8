package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.dto.RoomDTO;
import com.uade.tpo.demo.entity.dto.CreateRoomRequest;

import java.util.List;

public interface RoomService {
    List<RoomDTO> getUserRooms();
    RoomDTO createRoom(CreateRoomRequest request);
    RoomDTO updateRoom(Long roomId, CreateRoomRequest request);
    void deleteRoom(Long roomId);
    RoomDTO getRoomById(Long roomId);
}

