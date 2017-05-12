package be.ehb.model.responses;

import be.ehb.model.AbstractBaseNumericId;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PaygradeResponse extends AbstractBaseNumericId {

    private Double hourlyRate;

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "PayGradeResponse{" +
                super.toString() +
                ", hourly_rate=" + hourlyRate +
                '}';
    }
}