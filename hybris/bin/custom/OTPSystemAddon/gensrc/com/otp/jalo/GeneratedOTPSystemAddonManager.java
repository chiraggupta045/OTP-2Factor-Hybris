/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jul 4, 2020 5:15:01 AM                      ---
 * ----------------------------------------------------------------
 */
package com.otp.jalo;

import com.otp.constants.OTPSystemAddonConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>OTPSystemAddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedOTPSystemAddonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("secretKeyForOTP", AttributeMode.INITIAL);
		tmp.put("isEnabledTwoFactorAuthentication", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	@Override
	public String getName()
	{
		return OTPSystemAddonConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute.
	 * @return the isEnabledTwoFactorAuthentication
	 */
	public Boolean isIsEnabledTwoFactorAuthentication(final SessionContext ctx, final User item)
	{
		return (Boolean)item.getProperty( ctx, OTPSystemAddonConstants.Attributes.User.ISENABLEDTWOFACTORAUTHENTICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute.
	 * @return the isEnabledTwoFactorAuthentication
	 */
	public Boolean isIsEnabledTwoFactorAuthentication(final User item)
	{
		return isIsEnabledTwoFactorAuthentication( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute. 
	 * @return the isEnabledTwoFactorAuthentication
	 */
	public boolean isIsEnabledTwoFactorAuthenticationAsPrimitive(final SessionContext ctx, final User item)
	{
		Boolean value = isIsEnabledTwoFactorAuthentication( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute. 
	 * @return the isEnabledTwoFactorAuthentication
	 */
	public boolean isIsEnabledTwoFactorAuthenticationAsPrimitive(final User item)
	{
		return isIsEnabledTwoFactorAuthenticationAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute. 
	 * @param value the isEnabledTwoFactorAuthentication
	 */
	public void setIsEnabledTwoFactorAuthentication(final SessionContext ctx, final User item, final Boolean value)
	{
		item.setProperty(ctx, OTPSystemAddonConstants.Attributes.User.ISENABLEDTWOFACTORAUTHENTICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute. 
	 * @param value the isEnabledTwoFactorAuthentication
	 */
	public void setIsEnabledTwoFactorAuthentication(final User item, final Boolean value)
	{
		setIsEnabledTwoFactorAuthentication( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute. 
	 * @param value the isEnabledTwoFactorAuthentication
	 */
	public void setIsEnabledTwoFactorAuthentication(final SessionContext ctx, final User item, final boolean value)
	{
		setIsEnabledTwoFactorAuthentication( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.isEnabledTwoFactorAuthentication</code> attribute. 
	 * @param value the isEnabledTwoFactorAuthentication
	 */
	public void setIsEnabledTwoFactorAuthentication(final User item, final boolean value)
	{
		setIsEnabledTwoFactorAuthentication( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.secretKeyForOTP</code> attribute.
	 * @return the secretKeyForOTP
	 */
	public String getSecretKeyForOTP(final SessionContext ctx, final User item)
	{
		return (String)item.getProperty( ctx, OTPSystemAddonConstants.Attributes.User.SECRETKEYFOROTP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.secretKeyForOTP</code> attribute.
	 * @return the secretKeyForOTP
	 */
	public String getSecretKeyForOTP(final User item)
	{
		return getSecretKeyForOTP( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.secretKeyForOTP</code> attribute. 
	 * @param value the secretKeyForOTP
	 */
	public void setSecretKeyForOTP(final SessionContext ctx, final User item, final String value)
	{
		item.setProperty(ctx, OTPSystemAddonConstants.Attributes.User.SECRETKEYFOROTP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.secretKeyForOTP</code> attribute. 
	 * @param value the secretKeyForOTP
	 */
	public void setSecretKeyForOTP(final User item, final String value)
	{
		setSecretKeyForOTP( getSession().getSessionContext(), item, value );
	}
	
}
