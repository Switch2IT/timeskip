package be.ehb.model.backup;

import be.ehb.model.AbstractBaseNumericId;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PaygradeBackup extends AbstractBaseNumericId {

    private Double hourlyRate;

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "PaygradeBackup{" +
                super.toString() +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}