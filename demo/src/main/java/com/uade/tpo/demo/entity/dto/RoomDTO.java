package com.uade.tpo.demo.entity.dto;

import lombok.Data;
import java.util.Date;

@Data
public class RoomDTO {
    private Long roomId;
    private Long userId;
    private String name;
    private Date createdDate;
}

