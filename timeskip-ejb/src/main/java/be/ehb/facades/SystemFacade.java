package be.ehb.facades;

import be.ehb.configuration.IAppConfig;
import be.ehb.model.requests.RestoreBackupRequest;
import be.ehb.model.responses.BackUpResponse;
import be.ehb.model.responses.SystemComponentsStatus;
import be.ehb.model.responses.SystemStatusResponse;
import be.ehb.security.idp.IIdpClient;
import be.ehb.storage.IStorageService;
import be.ehb.utils.BackupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class SystemFacade implements ISystemFacade {

    private static final Logger log = LoggerFactory.getLogger(SystemFacade.class);

    @Inject
    private IStorageService storage;
    @Inject
    private IIdpClient idpClient;
    @Inject
    private IAppConfig appConfig;

    public SystemStatusResponse getStatus() {
        SystemStatusResponse rval = new SystemStatusResponse();
        rval.setId(ID);
        rval.setName(NAME);
        rval.setDescription(DESCRIPTION);
        rval.setMoreInfo(MORE_INFO);
        rval.setBuiltOn(appConfig.getBuildDate());
        rval.setVersion(appConfig.getVersion());
        SystemComponentsStatus compStatus = new SystemComponentsStatus();
        compStatus.setTimeskipApi(storage != null);
        compStatus.setKeycloakIdp(idpClient.getPublicKey(appConfig.getIdpRealm(), appConfig.getIdpKeystoreId()) != null);
        rval.setComponentsUp(compStatus);
        rval.setUp(compStatus.getKeycloakIdp() && compStatus.getTimeskipApi());
        return rval;
    }

    @Override
    public BackUpResponse getBackup() {
        return BackupUtil.createBackupResponse(
                storage.listOrganizations(),
                storage.listProjects(),
                storage.listActivities(),
                storage.listPaygrades(),
                storage.listUsers(null, null, null, null, null, null),
                storage.listRoles(),
                storage.listMemberships(),
                storage.listMailTemplates(),
                storage.listConfigs(),
                storage.listWorklogs()
        );
    }

    @Override
    public void restoreBackup(RestoreBackupRequest request) {
        // Validate the backup
        BackupUtil.validateBackup(request);
        storage.restore(request);
        log.info("Backup restored");
    }
}