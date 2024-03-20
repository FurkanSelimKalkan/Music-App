package com.application.music.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;


public class RatingDTO {


    @DecimalMax(value = "5.00", message = "Number cannot be Bigger than 5")
    @DecimalMin(value = "0.00", message = "Number cannot be smaller than 0")
    private BigDecimal rating;

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
