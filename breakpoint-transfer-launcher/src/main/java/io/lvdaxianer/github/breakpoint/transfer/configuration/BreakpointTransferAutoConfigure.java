package io.lvdaxianer.github.breakpoint.transfer.configuration;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties(UploadFileProperties.class)
@ConditionalOnMissingBean({UploadFileFullProperties.class, UploadFileProperties.class})
@ConditionalOnProperty(
        prefix = "io.lvdaxianer.upload.file",
        name = "enabled-type"
)
@ComponentScan("io.lvdaxianer.github.breakpoint.transfer")
@Configuration
public class BreakpointTransferAutoConfigure {
}
