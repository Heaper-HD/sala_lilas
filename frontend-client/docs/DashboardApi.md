# SalaLilsApi.DashboardApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**checkin**](DashboardApi.md#checkin) | **PATCH** /dashboard/agendamentos/{id}/checkin | Check-in do paciente - altera status para TRIAGEM
[**contadores**](DashboardApi.md#contadores) | **GET** /dashboard/contadores | Retorna contadores da fila do dia
[**listar2**](DashboardApi.md#listar2) | **GET** /dashboard/agendamentos | Lista agendamentos do dia com status AGENDADO
[**naoVeio**](DashboardApi.md#naoVeio) | **PATCH** /dashboard/agendamentos/{id}/nao-veio | Marca paciente como ausense - finaliza atendimento (irreversível)



## checkin

> AgendamentoDashboardResponse checkin(id)

Check-in do paciente - altera status para TRIAGEM

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.DashboardApi();
let id = "id_example"; // String | 
apiInstance.checkin(id).then((data) => {
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

[**AgendamentoDashboardResponse**](AgendamentoDashboardResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## contadores

> ContadoresResponse contadores()

Retorna contadores da fila do dia

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.DashboardApi();
apiInstance.contadores().then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters

This endpoint does not need any parameter.

### Return type

[**ContadoresResponse**](ContadoresResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## listar2

> [AgendamentoDashboardResponse] listar2(opts)

Lista agendamentos do dia com status AGENDADO

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.DashboardApi();
let opts = {
  'data': new Date("2013-10-20") // Date | 
};
apiInstance.listar2(opts).then((data) => {
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

[**[AgendamentoDashboardResponse]**](AgendamentoDashboardResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## naoVeio

> AgendamentoDashboardResponse naoVeio(id)

Marca paciente como ausense - finaliza atendimento (irreversível)

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.DashboardApi();
let id = "id_example"; // String | 
apiInstance.naoVeio(id).then((data) => {
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

[**AgendamentoDashboardResponse**](AgendamentoDashboardResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

