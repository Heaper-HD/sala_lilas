# SalaLilsApi.PacientesApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**buscar5**](PacientesApi.md#buscar5) | **GET** /pacientes/{pacienteId} | Retorna dados completos do paciente com todos os atendiemntos
[**listar1**](PacientesApi.md#listar1) | **GET** /pacientes | Lista todos os pacientes com histórico no sistema
[**timeline**](PacientesApi.md#timeline) | **GET** /pacientes/{pacienteId}/timeline | Retorna timeline cronológica completa do paciente



## buscar5

> PacienteDetalheResponse buscar5(pacienteId)

Retorna dados completos do paciente com todos os atendiemntos

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.PacientesApi();
let pacienteId = "pacienteId_example"; // String | 
apiInstance.buscar5(pacienteId).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pacienteId** | **String**|  | 

### Return type

[**PacienteDetalheResponse**](PacienteDetalheResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## listar1

> [PacienteSummaryResponse] listar1(opts)

Lista todos os pacientes com histórico no sistema

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.PacientesApi();
let opts = {
  'busca': "busca_example" // String | 
};
apiInstance.listar1(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **busca** | **String**|  | [optional] 

### Return type

[**[PacienteSummaryResponse]**](PacienteSummaryResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## timeline

> [TimelineEventoResponse] timeline(pacienteId)

Retorna timeline cronológica completa do paciente

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.PacientesApi();
let pacienteId = "pacienteId_example"; // String | 
apiInstance.timeline(pacienteId).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pacienteId** | **String**|  | 

### Return type

[**[TimelineEventoResponse]**](TimelineEventoResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

