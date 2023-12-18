package URFU_Music.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("storage")
public class StorageProperties {
    @Value("${storageLocation}")
    private String location;

    public void setLocation(String location) {
        this.location = location;
    }
}
