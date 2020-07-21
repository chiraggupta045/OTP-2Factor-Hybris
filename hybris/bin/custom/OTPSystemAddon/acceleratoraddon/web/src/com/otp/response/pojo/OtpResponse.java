/**
 *
 */
package com.otp.response.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResponse
{
	@JsonProperty("return")
	private String success;

	@JsonProperty("request_id")
	private String requestId;

	public String getSuccess()
	{
		return success;
	}

	public void setSuccess(final String success)
	{
		this.success = success;
	}

	public String getRequestId()
	{
		return requestId;
	}

	public void setRequestId(final String requestId)
	{
		this.requestId = requestId;
	}
}
