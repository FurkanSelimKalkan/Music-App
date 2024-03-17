package com.application.music.dto;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class RatingDTO {

    @DecimalMax(value = "5.00", message = "Number cannot be Bigger than 5")
    @DecimalMin(value = "0.00", message = "Number cannot be smaller than 0")
    private Double rating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
