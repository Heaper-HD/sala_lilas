# SalaLilsApi.AuthApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**aceitarLgpd**](AuthApi.md#aceitarLgpd) | **POST** /auth/lgpd/aceitar | Accept LGPD terms
[**login**](AuthApi.md#login) | **POST** /auth/login | Login with email and password
[**logout**](AuthApi.md#logout) | **POST** /auth/logout | Logout - invalid session
[**me**](AuthApi.md#me) | **GET** /auth/me | Get currect logged in user
[**refresh**](AuthApi.md#refresh) | **POST** /auth/refresh | Refresh access token



## aceitarLgpd

> LgpdResponse aceitarLgpd()

Accept LGPD terms

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AuthApi();
apiInstance.aceitarLgpd().then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters

This endpoint does not need any parameter.

### Return type

[**LgpdResponse**](LgpdResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## login

> LoginResponse login(loginRequest)

Login with email and password

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AuthApi();
let loginRequest = new SalaLilsApi.LoginRequest(); // LoginRequest | 
apiInstance.login(loginRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loginRequest** | [**LoginRequest**](LoginRequest.md)|  | 

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## logout

> logout()

Logout - invalid session

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AuthApi();
apiInstance.logout().then(() => {
  console.log('API called successfully.');
}, (error) => {
  console.error(error);
});

```

### Parameters

This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## me

> MeResponse me()

Get currect logged in user

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AuthApi();
apiInstance.me().then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters

This endpoint does not need any parameter.

### Return type

[**MeResponse**](MeResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## refresh

> RefreshResponse refresh(refreshRequest)

Refresh access token

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AuthApi();
let refreshRequest = new SalaLilsApi.RefreshRequest(); // RefreshRequest | 
apiInstance.refresh(refreshRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **refreshRequest** | [**RefreshRequest**](RefreshRequest.md)|  | 

### Return type

[**RefreshResponse**](RefreshResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

