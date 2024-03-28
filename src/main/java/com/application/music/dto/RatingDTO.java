package com.application.music.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatingDTO {

    @Min(value = 1, message = "Number cannot be smaller than 1")
    @Max(value = 5, message = "Number cannot be Bigger than 5")
    private Integer rating;
}
