package be.ehb.startup;

import be.ehb.configuration.IAppConfig;
import be.ehb.entities.config.ConfigBean;
import be.ehb.scheduler.ScheduleService;
import be.ehb.storage.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Singleton(name = "StartupService")
@DependsOn("AppConfig")
@Startup
public class StartupService {

    private static final Logger log = LoggerFactory.getLogger(StartupService.class);

    @Inject
    private IAppConfig appConfig;
    @Inject
    private IStorageService storage;
    @Inject
    private ScheduleService schedServ;

    @PostConstruct
    public void init() {
        try {
            // Insert startup tasks here, such as mailing tasks
            if (appConfig.getDayOfMonthlyReminderEmail() > 28 || appConfig.getDayOfMonthlyReminderEmail() < 1) {
                ConfigBean cfgb = storage.getDefaultConfig();
                cfgb.setDayOfMonthlyReminderEmail(28);
                storage.updateConfig(cfgb);
            }
            schedServ.ScheduleStart();
        } catch (Exception ex) {
            log.error("Error occured during startup: {}", ex);
        }
    }

}