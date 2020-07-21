package facade;

import com.otp.facade.impl.DefaultOtpFacade;
import com.otp.response.pojo.OtpResponse;
import com.otp.service.OtpIntegrationService;
import com.otp.service.otpgenerator.OTP;
import com.otp.service.otpgenerator.OTPEngine;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOtpFacadeTest {
    @InjectMocks
    DefaultOtpFacade defaultOtpFacade=new DefaultOtpFacade();
    @Mock
     OtpIntegrationService otpIntegrationService;
    @Mock
     ConfigurationService configurationService;
    @Mock
    Configuration configuration;
    @Mock
    SessionService sessionService;
    @Mock
    ResponseEntity<OtpResponse> responseEntity;

    @Test
    public void sendOtpForVerificationTest(){
        Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
        Mockito.when(configuration.getProperty(Matchers.anyString())).thenReturn("abc");
        Mockito.when( otpIntegrationService.sendOtpForVerification(Matchers.any(), Matchers.any(),
                Matchers.any())).thenReturn(responseEntity);
        defaultOtpFacade.sendOtpForVerification("882828282","123232");
    }
    @Test
    public void verifyingOtpTest(){
        Mockito.when(sessionService.getAttribute("otp")).thenReturn("123232");
        boolean flag=defaultOtpFacade.verifyingOtp("123232");
        Assert.assertEquals(true,flag);
    }

}
