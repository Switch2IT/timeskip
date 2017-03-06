package be.ehb.facades;

import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.responses.TokenClaimsResponse;
import be.ehb.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UserFacade implements IUserFacade {

    private static final Logger log = LoggerFactory.getLogger(UserFacade.class);

    @Override
    public TokenClaimsResponse parseJWT(JWTParseRequest jwt) {
        TokenClaimsResponse rVal = null;
        if (StringUtils.isNotEmpty(jwt.getJwt())) {
            try {
                JwtClaims claims = JWTUtils.getUnvalidatedContext(jwt.getJwt()).getJwtClaims();
                TokenClaimsResponse user = new TokenClaimsResponse();
                user.setUserInfo(claims.getClaimsMap());
                rVal = user;
            } catch (InvalidJwtException | MalformedClaimException e) {
                log.error("Failed to parse JWT: {}", jwt);
                e.printStackTrace();

            }
        }
        return rVal;
    }


}