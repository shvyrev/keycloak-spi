package spi.keycloak.rest;

import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resources.Cors;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionCountResource {
    private KeycloakSession session;

    public SessionCountResource() {
    }

    public SessionCountResource(KeycloakSession session) {
        this.session = session;
    }

    @OPTIONS
    @Path("{any:.*}")
    public Response preflight() {
        HttpRequest request = session.getContext().getContextObject(HttpRequest.class);
        return Cors.add(request, Response.ok()).auth().preflight().build();
    }

    @GET
    @Path("sessions-count")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Long> getSessionsCount(){
        AuthenticationManager.AuthResult auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();

        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        }

        if (auth.getToken().getIssuedFor() == null || auth.getToken().getRealmAccess() == null) {
            throw new ForbiddenException("Does not enough permissions to fetch users.");
        }

        RealmModel realm = auth.getSession().getRealm();
        return session.sessions().getUserSessionsStream(realm, auth.getClient())
                .map(userSessionModel -> userSessionModel.getUser().getUsername())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

}
