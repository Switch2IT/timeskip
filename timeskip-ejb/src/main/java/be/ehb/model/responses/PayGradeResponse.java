package be.ehb.model.responses;

import be.ehb.model.AbstractBaseResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PayGradeResponse extends AbstractBaseResponse {

    private Double hourly_rate;

    @Override
    public String toString() {
        return "PayGradeResponse{" +
                super.toString() +
                ", hourly_rate=" + hourly_rate +
                '}';
    }
}