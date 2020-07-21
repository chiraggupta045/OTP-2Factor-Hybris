package facade;

import com.otp.facade.impl.DefaultSecretKeyFacade;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.apache.commons.configuration.Configuration;
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSecretKeyFacadeTest {
    @InjectMocks
    DefaultSecretKeyFacade defaultSecretKeyFacade=new DefaultSecretKeyFacade();
    @Mock
    UserService userService;
    @Mock
    ModelService modelService;
    @Mock
    ConfigurationService configurationService;
    @Mock
    Configuration configuration;
    UserModel userModel;
    @Before
    public void setUp(){
        userModel=new UserModel();

    }
    @Test
  public  void checkUserAuthenticationIfTwoFactorEnabledTest() throws Exception {
        userModel.setIsEnabledTwoFactorAuthentication(true);
        Mockito.when(userService.getCurrentUser()).thenReturn(userModel);
        boolean flag= defaultSecretKeyFacade.checkUserAuthentication();
        Assert.assertEquals(true,flag);
    }
    @Test
    public  void checkUserAuthenticationIfTwoFactorNotEnabledTest() throws Exception{
        userModel.setIsEnabledTwoFactorAuthentication(false);
        Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
        userModel.setUid("abc@test.com");
        Mockito.when(userService.getCurrentUser()).thenReturn(userModel);
        boolean flag= defaultSecretKeyFacade.checkUserAuthentication();
        Assert.assertEquals(false,flag);
    }

}
