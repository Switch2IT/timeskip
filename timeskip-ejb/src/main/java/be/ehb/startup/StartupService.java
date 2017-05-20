package be.ehb.startup;

import be.ehb.mail.IMailService;
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
    private IStorageService storage;
    @Inject
    private IMailService mailService;
    @Inject
    private ScheduleService schedServ;

    @PostConstruct
    public void init() {
        try {
            // Insert startup tasks here, such as mailing tasks
            sendStartupMail();
            schedServ.ScheduleStart();
        } catch (Exception ex) {
            log.error("Error occured during startup: {}", ex);
        }
    }

    private void sendStartupMail() {
        try {
            mailService.sendStartupMail();
        } catch (Exception ex) {
            log.error("Failed to send startup mail: {}", ex);
        }
    }

}