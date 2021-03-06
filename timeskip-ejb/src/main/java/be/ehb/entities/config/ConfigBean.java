package be.ehb.entities.config;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "config", schema = "timeskip")
public class ConfigBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "config_path", nullable = false)
    private String configPath;
    @Column(name = "default_config")
    private Boolean defaultConfig;
    @Column(name = "day_of_monthly_reminder_email")
    private Integer dayOfMonthlyReminderEmail;
    @Column(name = "last_day_of_month")
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
        if (!(o instanceof ConfigBean)) return false;

        ConfigBean that = (ConfigBean) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "id=" + id +
                ", configPath='" + configPath + '\'' +
                ", defaultConfig=" + defaultConfig +
                ", dayOfMonthlyReminderEmail=" + dayOfMonthlyReminderEmail +
                ", lastDayOfMonth=" + lastDayOfMonth +
                '}';
    }
}