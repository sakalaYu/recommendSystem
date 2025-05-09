package org.example.entity;

import lombok.Data;

@Data
public class Rate {
    private Integer id;
    private Integer file_id;
    private Integer user_id;
    private Double rating;
}