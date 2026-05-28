# SalaLilsApi.FilasApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**filaJuridico**](FilasApi.md#filaJuridico) | **GET** /filas/juridico | Fila do NPJ — atendimentos com status JURIDICO
[**filaPsicologia**](FilasApi.md#filaPsicologia) | **GET** /filas/psicologia | Fila do CIS — atendimentos com status PSICOLOGIA
[**filaTecnica**](FilasApi.md#filaTecnica) | **GET** /filas/tecnica | Fila da Equipe Técnica — atendimentos com status TECNICA



## filaJuridico

> [FilaItemResponse] filaJuridico(opts)

Fila do NPJ — atendimentos com status JURIDICO

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.FilasApi();
let opts = {
  'data': new Date("2013-10-20") // Date | 
};
apiInstance.filaJuridico(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data** | **Date**|  | [optional] 

### Return type

[**[FilaItemResponse]**](FilaItemResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## filaPsicologia

> [FilaItemResponse] filaPsicologia(opts)

Fila do CIS — atendimentos com status PSICOLOGIA

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.FilasApi();
let opts = {
  'data': new Date("2013-10-20") // Date | 
};
apiInstance.filaPsicologia(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data** | **Date**|  | [optional] 

### Return type

[**[FilaItemResponse]**](FilaItemResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## filaTecnica

> [FilaItemResponse] filaTecnica(opts)

Fila da Equipe Técnica — atendimentos com status TECNICA

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.FilasApi();
let opts = {
  'data': new Date("2013-10-20") // Date | 
};
apiInstance.filaTecnica(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data** | **Date**|  | [optional] 

### Return type

[**[FilaItemResponse]**](FilaItemResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

