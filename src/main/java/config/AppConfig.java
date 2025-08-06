package config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import security.JwtFilter;

@ApplicationPath("/api")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("resource");
        register(JwtFilter.class);
    }
}
