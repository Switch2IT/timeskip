package be.ehb.model.backup;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationBackup implements Serializable {

    private Long id;
    private String configPath;
    private Boolean defaultConfig;
    private Integer dayOfMonthlyReminderEmail;
    private Boolean lastDayOfMonth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public Boolean getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(Boolean defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public Integer getDayOfMonthlyReminderEmail() {
        return dayOfMonthlyReminderEmail;
    }

    public void setDayOfMonthlyReminderEmail(Integer dayOfMonthlyReminderEmail) {
        this.dayOfMonthlyReminderEmail = dayOfMonthlyReminderEmail;
    }

    public Boolean getLastDayOfMonth() {
        return lastDayOfMonth;
    }

    public void setLastDayOfMonth(Boolean lastDayOfMonth) {
        this.lastDayOfMonth = lastDayOfMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigurationBackup)) return false;

        ConfigurationBackup that = (ConfigurationBackup) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ConfigurationBackup{" +
                "id=" + id +
                ", configPath='" + configPath + '\'' +
                ", defaultConfig=" + defaultConfig +
                ", dayOfMonthlyReminderEmail=" + dayOfMonthlyReminderEmail +
                ", lastDayOfMonth=" + lastDayOfMonth +
                '}';
    }
}