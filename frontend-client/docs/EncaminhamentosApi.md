# SalaLilsApi.EncaminhamentosApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**finalizar**](EncaminhamentosApi.md#finalizar) | **POST** /encaminhamentos/{id}/finalizar | Finaliza atendimento sem encaminhar - uso exclusivo da Equipe Técnica
[**paraJuridico**](EncaminhamentosApi.md#paraJuridico) | **POST** /encaminhamentos/{id}/juridico | Encaminha para NPJ (Jurídico)
[**paraOutros**](EncaminhamentosApi.md#paraOutros) | **POST** /encaminhamentos/{id}/outros | Encaminha para Outros - finaliza atendimento e gera PDF
[**paraPsicologia**](EncaminhamentosApi.md#paraPsicologia) | **POST** /encaminhamentos/{id}/psicologia | Encaminha para CIS (Psicologia)
[**paraTecnica**](EncaminhamentosApi.md#paraTecnica) | **POST** /encaminhamentos/{id}/tecnica | Encaminha para Equipe Técnica



## finalizar

> EncaminhamentoResponse finalizar(id)

Finaliza atendimento sem encaminhar - uso exclusivo da Equipe Técnica

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.EncaminhamentosApi();
let id = "id_example"; // String | 
apiInstance.finalizar(id).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

[**EncaminhamentoResponse**](EncaminhamentoResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## paraJuridico

> EncaminhamentoResponse paraJuridico(id)

Encaminha para NPJ (Jurídico)

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.EncaminhamentosApi();
let id = "id_example"; // String | 
apiInstance.paraJuridico(id).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

[**EncaminhamentoResponse**](EncaminhamentoResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## paraOutros

> EncaminhamentoOutrosResponse paraOutros(id)

Encaminha para Outros - finaliza atendimento e gera PDF

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.EncaminhamentosApi();
let id = "id_example"; // String | 
apiInstance.paraOutros(id).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

[**EncaminhamentoOutrosResponse**](EncaminhamentoOutrosResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## paraPsicologia

> EncaminhamentoResponse paraPsicologia(id)

Encaminha para CIS (Psicologia)

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.EncaminhamentosApi();
let id = "id_example"; // String | 
apiInstance.paraPsicologia(id).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

[**EncaminhamentoResponse**](EncaminhamentoResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## paraTecnica

> EncaminhamentoResponse paraTecnica(id)

Encaminha para Equipe Técnica

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.EncaminhamentosApi();
let id = "id_example"; // String | 
apiInstance.paraTecnica(id).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

[**EncaminhamentoResponse**](EncaminhamentoResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

