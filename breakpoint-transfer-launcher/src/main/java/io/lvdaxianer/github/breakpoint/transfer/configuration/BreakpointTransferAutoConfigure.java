package io.lvdaxianer.github.breakpoint.transfer.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ConditionalOnProperty(
        prefix = "io.lvdaxianer.upload.file",
        name = "enabled-type"
)
@ComponentScan("io.lvdaxianer.github.breakpoint.transfer")
@Configuration
public class BreakpointTransferAutoConfigure {
}
