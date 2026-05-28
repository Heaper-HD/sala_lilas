# SalaLilsApi.ProntuarioPsicossocialApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**atualizar1**](ProntuarioPsicossocialApi.md#atualizar1) | **PUT** /prontuarios/{agendamentoId} | Atualiza prontuário - somente enquanto status &#x3D; PSICOLOGIA
[**buscar1**](ProntuarioPsicossocialApi.md#buscar1) | **GET** /prontuarios/{agendamentoId} | Busca prontuário psicossocial de um atendimento
[**criar**](ProntuarioPsicossocialApi.md#criar) | **POST** /prontuarios/{agendamentoId} | Cria prontuário - somente CIS, status deve ser PSICOLOGIA



## atualizar1

> ProntuarioResponse atualizar1(agendamentoId, prontuarioRequest)

Atualiza prontuário - somente enquanto status &#x3D; PSICOLOGIA

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.ProntuarioPsicossocialApi();
let agendamentoId = "agendamentoId_example"; // String | 
let prontuarioRequest = new SalaLilsApi.ProntuarioRequest(); // ProntuarioRequest | 
apiInstance.atualizar1(agendamentoId, prontuarioRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **prontuarioRequest** | [**ProntuarioRequest**](ProntuarioRequest.md)|  | 

### Return type

[**ProntuarioResponse**](ProntuarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## buscar1

> ProntuarioResponse buscar1(agendamentoId)

Busca prontuário psicossocial de um atendimento

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.ProntuarioPsicossocialApi();
let agendamentoId = "agendamentoId_example"; // String | 
apiInstance.buscar1(agendamentoId).then((data) => {
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

[**ProntuarioResponse**](ProntuarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## criar

> ProntuarioResponse criar(agendamentoId, prontuarioRequest)

Cria prontuário - somente CIS, status deve ser PSICOLOGIA

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.ProntuarioPsicossocialApi();
let agendamentoId = "agendamentoId_example"; // String | 
let prontuarioRequest = new SalaLilsApi.ProntuarioRequest(); // ProntuarioRequest | 
apiInstance.criar(agendamentoId, prontuarioRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **prontuarioRequest** | [**ProntuarioRequest**](ProntuarioRequest.md)|  | 

### Return type

[**ProntuarioResponse**](ProntuarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

