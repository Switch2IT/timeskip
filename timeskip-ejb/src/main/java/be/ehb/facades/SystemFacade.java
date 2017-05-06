package be.ehb.facades;

import be.ehb.configuration.IAppConfig;
import be.ehb.model.responses.SystemComponentsStatus;
import be.ehb.model.responses.SystemStatusResponse;
import be.ehb.security.idp.IIdpClient;
import be.ehb.storage.IStorageService;

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

    @Inject
    private IStorageService storage;
    @Inject
    private IIdpClient idpClient;
    @Inject
    private IAppConfig config;

    public SystemStatusResponse getStatus() {
        SystemStatusResponse rval = new SystemStatusResponse();
        rval.setId(ID);
        rval.setName(NAME);
        rval.setDescription(DESCRIPTION);
        rval.setMoreInfo(MORE_INFO);
        rval.setBuiltOn(config.getBuildDate());
        rval.setVersion(config.getVersion());
        SystemComponentsStatus compStatus = new SystemComponentsStatus();
        compStatus.setTimeskipApi(storage != null);
        compStatus.setKeycloakIdp(idpClient.getPublicKey(config.getIdpRealm(), config.getIdpKeystoreId()) != null);
        rval.setComponentsUp(compStatus);
        rval.setUp(compStatus.getKeycloakIdp() && compStatus.getTimeskipApi());
        return rval;
    }
}