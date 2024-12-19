package spring.sample.config;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Slf4j
@Configuration
public class JdbiConfig {

    @Bean
    public Jdbi init(final DataSource dataSource, final List<JdbiPlugin> jdbiPlugins, final List<RowMapper<?>> rowMappers) {
        final TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(dataSource);
        final Jdbi jdbi = Jdbi.create(proxy);

        log.info("install jdbiPlugins, total: {}", Optional.ofNullable(jdbiPlugins).orElse(List.of()).size());
        Optional.ofNullable(jdbiPlugins).orElse(List.of()).forEach(jdbi::installPlugin);

        log.info("install rowMappers, total: {}", Optional.ofNullable(rowMappers).orElse(List.of()).size());
        Optional.ofNullable(rowMappers).orElse(List.of()).forEach(jdbi::registerRowMapper);

        jdbi.installPlugins();

        return jdbi;
    }
}
