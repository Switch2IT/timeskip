package be.ehb.model.requests;

import be.ehb.model.AbstractBaseRequest;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UpdatePaygradeRequest extends AbstractBaseRequest {

    private Double hourlyRate;

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "UpdatePaygradeRequest{" +
                "hourlyRate=" + hourlyRate +
                '}';
    }
}