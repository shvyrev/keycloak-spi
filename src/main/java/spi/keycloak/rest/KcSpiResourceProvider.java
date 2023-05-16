package spi.keycloak.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class KcSpiResourceProvider implements RealmResourceProvider {
    private KeycloakSession session;

    public KcSpiResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return new SessionCountResource(session);
    }

    @Override
    public void close() {

    }
}
