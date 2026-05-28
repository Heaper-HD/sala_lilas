# SalaLilsApi.AnamneseInicialApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**atualizar4**](AnamneseInicialApi.md#atualizar4) | **PUT** /anamnese-inicial/{agendamentoId} | Atualiza anamnese inicial - somente enquanto status &#x3D; TRIAGEM
[**buscar4**](AnamneseInicialApi.md#buscar4) | **GET** /anamnese-inicial/{agendamentoId} | Busca anamnese inicial de um atendimento
[**criar3**](AnamneseInicialApi.md#criar3) | **POST** /anamnese-inicial/{agendamentoId} | Cria anamnese inicial - somente Atendente, status deve ser TRIAGEM



## atualizar4

> AnamneseInicialResponse atualizar4(agendamentoId, anamneseInicialRequest)

Atualiza anamnese inicial - somente enquanto status &#x3D; TRIAGEM

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AnamneseInicialApi();
let agendamentoId = "agendamentoId_example"; // String | 
let anamneseInicialRequest = new SalaLilsApi.AnamneseInicialRequest(); // AnamneseInicialRequest | 
apiInstance.atualizar4(agendamentoId, anamneseInicialRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **anamneseInicialRequest** | [**AnamneseInicialRequest**](AnamneseInicialRequest.md)|  | 

### Return type

[**AnamneseInicialResponse**](AnamneseInicialResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## buscar4

> AnamneseInicialResponse buscar4(agendamentoId)

Busca anamnese inicial de um atendimento

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AnamneseInicialApi();
let agendamentoId = "agendamentoId_example"; // String | 
apiInstance.buscar4(agendamentoId).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 

### Return type

[**AnamneseInicialResponse**](AnamneseInicialResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## criar3

> AnamneseInicialResponse criar3(agendamentoId, anamneseInicialRequest)

Cria anamnese inicial - somente Atendente, status deve ser TRIAGEM

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AnamneseInicialApi();
let agendamentoId = "agendamentoId_example"; // String | 
let anamneseInicialRequest = new SalaLilsApi.AnamneseInicialRequest(); // AnamneseInicialRequest | 
apiInstance.criar3(agendamentoId, anamneseInicialRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **anamneseInicialRequest** | [**AnamneseInicialRequest**](AnamneseInicialRequest.md)|  | 

### Return type

[**AnamneseInicialResponse**](AnamneseInicialResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

