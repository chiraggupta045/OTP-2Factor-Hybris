package service;

import com.otp.response.pojo.OtpResponse;
import com.otp.service.impl.DefaultIntegrationService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultIntegrationServiceTest {
    @InjectMocks
    DefaultIntegrationService defaultIntegrationService=new DefaultIntegrationService();
    @Mock
    ConfigurationService configurationService;
    @Mock
    Configuration configuration;
    @Mock
    RestTemplate restTemplate;
    @Mock
    ResponseEntity<OtpResponse> responseEntity;
    
    @Test
   public void sendOtpForVerificationTest(){
        Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
        Mockito.when(configuration.getProperty(Matchers.anyString())).thenReturn("url");
        Mockito.when(restTemplate.exchange(Matchers.any(),Matchers.any(),Matchers.any(), (Class<Object>) Matchers.anyObject())).thenReturn();
        
    }
}
