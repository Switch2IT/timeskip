package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayOfMonthlyReminderResponse implements Serializable {

    private Integer dayOfMonthlyReminder;
    private Boolean lastDayOfMonth;

    public Integer getDayOfMonthlyReminder() {
        return dayOfMonthlyReminder;
    }

    public void setDayOfMonthlyReminder(Integer dayOfMonthlyReminder) {
        this.dayOfMonthlyReminder = dayOfMonthlyReminder;
    }

    public Boolean getLastDayOfMonth() {
        return lastDayOfMonth;
    }

    public void setLastDayOfMonth(Boolean lastDayOfMonth) {
        this.lastDayOfMonth = lastDayOfMonth;
    }

    @Override
    public String toString() {
        return "DayOfMonthlyReminderResponse{" +
                "dayOfMonthlyReminder=" + dayOfMonthlyReminder +
                ", lastDayOfMonth=" + lastDayOfMonth +
                '}';
    }
}