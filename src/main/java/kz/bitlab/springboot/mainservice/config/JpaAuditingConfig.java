package kz.bitlab.springboot.mainservice.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Вынесено в отдельный класс, а не в MainServiceApplication,
// чтобы не ломать @WebMvcTest
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}