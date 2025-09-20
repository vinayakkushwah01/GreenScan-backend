package com.greenscan.dto.base;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseDTO {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;

}
