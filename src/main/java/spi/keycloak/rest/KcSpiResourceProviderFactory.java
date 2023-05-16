package spi.keycloak.rest;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class KcSpiResourceProviderFactory implements RealmResourceProviderFactory {

    public static final String PROVIDER_ID = "kc-spi-rest";
    private static final Logger log = Logger.getLogger( KcSpiResourceProviderFactory.class );

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new KcSpiResourceProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
        log.info("$ "+ "init() called with: config = [" + config + "]");
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        log.info("$ "+ "postInit() called with: factory = [" + factory + "]");
    }

    @Override
    public void close() {
        log.info("$ "+ "close() called");

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
