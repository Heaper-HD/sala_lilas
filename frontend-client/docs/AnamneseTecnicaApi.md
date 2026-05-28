# SalaLilsApi.AnamneseTecnicaApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**atualizar3**](AnamneseTecnicaApi.md#atualizar3) | **PUT** /anamnese-tecnica/{agendamentoId} | Atualiza anamnese técnica - somente quando status &#x3D; TECNICA
[**buscar3**](AnamneseTecnicaApi.md#buscar3) | **GET** /anamnese-tecnica/{agendamentoId} | Busca anamnese técnica - ATENDENTE recebe 403
[**criar2**](AnamneseTecnicaApi.md#criar2) | **POST** /anamnese-tecnica/{agendamentoId} | Cria anamnese técnica - somente equipe Técnica, status deve ser TECNICA



## atualizar3

> AnamneseTecnicaResponse atualizar3(agendamentoId, anamneseTecnicaRequest)

Atualiza anamnese técnica - somente quando status &#x3D; TECNICA

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AnamneseTecnicaApi();
let agendamentoId = "agendamentoId_example"; // String | 
let anamneseTecnicaRequest = new SalaLilsApi.AnamneseTecnicaRequest(); // AnamneseTecnicaRequest | 
apiInstance.atualizar3(agendamentoId, anamneseTecnicaRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **anamneseTecnicaRequest** | [**AnamneseTecnicaRequest**](AnamneseTecnicaRequest.md)|  | 

### Return type

[**AnamneseTecnicaResponse**](AnamneseTecnicaResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## buscar3

> AnamneseTecnicaResponse buscar3(agendamentoId)

Busca anamnese técnica - ATENDENTE recebe 403

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AnamneseTecnicaApi();
let agendamentoId = "agendamentoId_example"; // String | 
apiInstance.buscar3(agendamentoId).then((data) => {
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

[**AnamneseTecnicaResponse**](AnamneseTecnicaResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## criar2

> AnamneseTecnicaResponse criar2(agendamentoId, anamneseTecnicaRequest)

Cria anamnese técnica - somente equipe Técnica, status deve ser TECNICA

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.AnamneseTecnicaApi();
let agendamentoId = "agendamentoId_example"; // String | 
let anamneseTecnicaRequest = new SalaLilsApi.AnamneseTecnicaRequest(); // AnamneseTecnicaRequest | 
apiInstance.criar2(agendamentoId, anamneseTecnicaRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **anamneseTecnicaRequest** | [**AnamneseTecnicaRequest**](AnamneseTecnicaRequest.md)|  | 

### Return type

[**AnamneseTecnicaResponse**](AnamneseTecnicaResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

