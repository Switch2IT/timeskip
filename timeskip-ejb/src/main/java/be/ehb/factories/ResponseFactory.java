package be.ehb.factories;

import be.ehb.entities.identity.UserBean;
import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.model.responses.MembershipResponse;
import be.ehb.model.responses.RoleResponse;
import be.ehb.model.responses.UserResponse;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ResponseFactory {

    public static Response buildResponse(int httpCode, String headerName, String headerValue, Object entity) {
        Response.ResponseBuilder builder = Response.status(httpCode);
        if (StringUtils.isNotEmpty(headerName) && StringUtils.isNotEmpty(headerValue)) {
            builder.header(headerName, headerValue);
        }
        if (entity != null) {
            builder.entity(entity);
        }
        return builder.type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static Response buildResponse(int httpCode) {
        return buildResponse(httpCode, null, null, null);
    }

    public static Response buildResponse(int httpCode, Object entity) {
        return buildResponse(httpCode, null, null, entity);
    }

    public static UserResponse createUserResponse(UserBean user) {
        UserResponse rval = null;
        if (user != null) {
            rval = new UserResponse();
            rval.setId(user.getId());
            rval.setName(user.getName());
            rval.setEmail(user.getEmail());
            rval.setSurname(user.getSurname());
            rval.setDefaultHoursPerDay(user.getDefaultHoursPerDay());
            rval.setWorkDays(user.getWorkDays());
            rval.setMemberships(createMembershipResponses(user.getMemberships()));
            rval.setAdmin(user.getAdmin());
        }
        return rval;
    }

    public static MembershipResponse createMembershipResponse(MembershipBean membership) {
        MembershipResponse rval = null;
        if (membership != null) {
            rval = new MembershipResponse();
            rval.setOrganizationId(membership.getOrganizationId());
            rval.setRole(membership.getRoleId());
        }
        return rval;
    }

    public static RoleResponse createRoleResponse(RoleBean role) {
        RoleResponse rval = null;
        if (role != null) {
            rval = new RoleResponse();
            rval.setId(role.getId());
            rval.setName(role.getName());
            rval.setDescription(role.getDescription());
            rval.setAutoGrant(role.getAutoGrant());
        }
        return rval;
    }

    private static List<MembershipResponse> createMembershipResponses(List<MembershipBean> memberships) {
        return memberships.stream().map(ResponseFactory::createMembershipResponse).collect(Collectors.toList());
    }
}