package be.ehb.facades;

import be.ehb.model.responses.SystemStatusResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface ISystemFacade {

    String DESCRIPTION = "The Timeskip API allows authenticated users to log minutes on various project activities";
    String ID = "timeskip-api";
    String MORE_INFO = "https://www.erasmushogeschool.be";
    String NAME = "Timeskip REST API";

    SystemStatusResponse getStatus();
}