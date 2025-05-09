package org.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Download {
    private Integer id;
    private Integer userId;
    private Integer fileId;
    private LocalDateTime downloadTime;
}