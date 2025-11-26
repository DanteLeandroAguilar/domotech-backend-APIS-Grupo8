package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Room;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.dto.RoomDTO;
import com.uade.tpo.demo.entity.dto.CreateRoomRequest;
import com.uade.tpo.demo.exceptions.UserNotFoundException;
import com.uade.tpo.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<RoomDTO> getUserRooms() {
        User user = userService.getLoggedUser();
        List<Room> rooms = roomRepository.findByUser(user);
        return rooms.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional
    public RoomDTO createRoom(CreateRoomRequest request) {
        User user = userService.getLoggedUser();
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la habitación no puede estar vacío");
        }
        
        String normalizedName = request.getName().trim().toLowerCase();
        if (normalizedName.length() > 50) {
            normalizedName = normalizedName.substring(0, 50);
        }
        
        // Verificar si ya existe una habitación con ese nombre para el usuario
        Optional<Room> existingRoom = roomRepository.findByUserAndName(user, normalizedName);
        if (existingRoom.isPresent()) {
            throw new IllegalArgumentException("Ya existe una habitación con ese nombre");
        }
        
        Room room = new Room();
        room.setUser(user);
        room.setName(normalizedName);
        
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    @Override
    @Transactional
    public RoomDTO updateRoom(Long roomId, CreateRoomRequest request) {
        User user = userService.getLoggedUser();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada"));
        
        // Verificar que la habitación pertenece al usuario
        if (!room.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("No tienes permiso para modificar esta habitación");
        }
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la habitación no puede estar vacío");
        }
        
        String normalizedName = request.getName().trim().toLowerCase();
        if (normalizedName.length() > 50) {
            normalizedName = normalizedName.substring(0, 50);
        }
        
        // Verificar si ya existe otra habitación con ese nombre para el usuario
        Optional<Room> existingRoom = roomRepository.findByUserAndName(user, normalizedName);
        if (existingRoom.isPresent() && !existingRoom.get().getRoomId().equals(roomId)) {
            throw new IllegalArgumentException("Ya existe una habitación con ese nombre");
        }
        
        room.setName(normalizedName);
        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        User user = userService.getLoggedUser();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada"));
        
        // Verificar que la habitación pertenece al usuario
        if (!room.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("No tienes permiso para eliminar esta habitación");
        }
        
        // No permitir eliminar la habitación "general" si existe
        if ("general".equals(room.getName())) {
            throw new IllegalArgumentException("No se puede eliminar la habitación 'general'");
        }
        
        roomRepository.delete(room);
    }

    @Override
    public RoomDTO getRoomById(Long roomId) {
        User user = userService.getLoggedUser();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada"));
        
        // Verificar que la habitación pertenece al usuario
        if (!room.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("No tienes permiso para ver esta habitación");
        }
        
        return convertToDTO(room);
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setUserId(room.getUser().getUserId());
        dto.setName(room.getName());
        dto.setCreatedDate(room.getCreatedDate());
        return dto;
    }
}

