package ivan.rest.example.configuration;

import ivan.rest.example.client.ExternalClient;
import ivan.rest.example.test.clients.RestClient;
import ivan.rest.example.test.clients.WireMockClient;
import ivan.rest.example.test.utils.session.SessionImpl;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource(value = "classpath:application-test.yml")
@ComponentScan("ivan.rest.example.test")
@Import({SessionImpl.class, WireMockClient.class, RestClient.class})
public class TestConfig {

  @Spy
  private ExternalClient externalClient;

//  @LocalServerPort private int port;

//  @Value("${employee.service.host}")
//  private String employeeServiceHost;

  @PostConstruct
  void init() {
    MockitoAnnotations.openMocks(this);
//    baseUrl = String.format("http://%s:%s", employeeServiceHost, port);
  }

//  public static String baseUrl;

  @Bean
  @Primary
  ExternalClient getExternalClient() {
    return this.externalClient;
  }
}
