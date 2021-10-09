package com.practice.aws;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.nimbusds.jwt.JWTClaimsSet;
/**
 * Responsible to prepare AWS cognito user's claims/principal based valid JWT token
 * @author manish.luste
 */
public class AwsCognitoJwtAuthentication extends AbstractAuthenticationToken {
   
	private static final long serialVersionUID = 1L;
	
	private final transient Object principal;
    private JWTClaimsSet jwtClaimsSet;

    public AwsCognitoJwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.jwtClaimsSet = jwtClaimsSet;
        super.setAuthenticated(true);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((jwtClaimsSet == null) ? 0 : jwtClaimsSet.hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AwsCognitoJwtAuthentication other = (AwsCognitoJwtAuthentication) obj;
		if (jwtClaimsSet == null) {
			if (other.jwtClaimsSet != null)
				return false;
		} else if (!jwtClaimsSet.equals(other.jwtClaimsSet))
			return false;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		return true;
	}

	public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public JWTClaimsSet getJwtClaimsSet() {
        return this.jwtClaimsSet;
    }
}
