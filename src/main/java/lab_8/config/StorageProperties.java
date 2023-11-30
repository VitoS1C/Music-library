package lab_8.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.module.Configuration;

@Getter
@ConfigurationProperties("storage")
public class StorageProperties {
    @Value("${storageLocation}")
    private String location;

    public void setLocation(String location) {
        this.location = location;
    }
}
