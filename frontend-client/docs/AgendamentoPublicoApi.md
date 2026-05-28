# SalaLilsApi.AgendamentoPublicoApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**criar5**](AgendamentoPublicoApi.md#criar5) | **POST** /agendamentos/publico | Cria um novo agendamento externo
[**horarios**](AgendamentoPublicoApi.md#horarios) | **GET** /agendamentos/publico/horarios | Lista horários disponíveis para uma data



## criar5

> AgendamentoPublicoReponse criar5(agendamentoPublicoRequest)

Cria um novo agendamento externo

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AgendamentoPublicoApi();
let agendamentoPublicoRequest = new SalaLilsApi.AgendamentoPublicoRequest(); // AgendamentoPublicoRequest | 
apiInstance.criar5(agendamentoPublicoRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoPublicoRequest** | [**AgendamentoPublicoRequest**](AgendamentoPublicoRequest.md)|  | 

### Return type

[**AgendamentoPublicoReponse**](AgendamentoPublicoReponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## horarios

> HorariosDisponiveisResponse horarios(data)

Lista horários disponíveis para uma data

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AgendamentoPublicoApi();
let data = new Date("2013-10-20"); // Date | 
apiInstance.horarios(data).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data** | **Date**|  | 

### Return type

[**HorariosDisponiveisResponse**](HorariosDisponiveisResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

