package com.solace.labs.sempclient.samplelib.api;

import com.solace.labs.sempclient.samplelib.ApiException;
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.Configuration;
import com.solace.labs.sempclient.samplelib.Pair;

import javax.ws.rs.core.GenericType;

import com.solace.labs.sempclient.samplelib.model.SempMetaOnlyResponse;
import com.solace.labs.sempclient.samplelib.model.SystemInformationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class SystemInformationApi {
  private ApiClient apiClient;

  public SystemInformationApi() {
    this(Configuration.getDefaultApiClient());
  }

  public SystemInformationApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get a System Information object.
   * Get a System Information object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation.  This has been available since 2.1.0.  This has been deprecated since 2.2.0.
   * @return SystemInformationResponse
   * @throws ApiException if fails to make API call
   */
  public SystemInformationResponse getSystemInformation() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/systemInformation".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<SystemInformationResponse> localVarReturnType = new GenericType<SystemInformationResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
