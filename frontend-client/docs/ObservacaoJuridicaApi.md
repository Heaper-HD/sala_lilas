# SalaLilsApi.ObservacaoJuridicaApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**atualizar2**](ObservacaoJuridicaApi.md#atualizar2) | **PUT** /obs-juridicas/{agendamentoId} | Atualiza observação jurídica — somente enquanto status &#x3D; JURIDICO
[**buscar2**](ObservacaoJuridicaApi.md#buscar2) | **GET** /obs-juridicas/{agendamentoId} | Busca observação jurídica de um atendimento
[**criar1**](ObservacaoJuridicaApi.md#criar1) | **POST** /obs-juridicas/{agendamentoId} | Cria observação jurídica — somente NPJ, status deve ser JURIDICO



## atualizar2

> ObsJuridicaResponse atualizar2(agendamentoId, obsJuridicaRequest)

Atualiza observação jurídica — somente enquanto status &#x3D; JURIDICO

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.ObservacaoJuridicaApi();
let agendamentoId = "agendamentoId_example"; // String | 
let obsJuridicaRequest = new SalaLilsApi.ObsJuridicaRequest(); // ObsJuridicaRequest | 
apiInstance.atualizar2(agendamentoId, obsJuridicaRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **obsJuridicaRequest** | [**ObsJuridicaRequest**](ObsJuridicaRequest.md)|  | 

### Return type

[**ObsJuridicaResponse**](ObsJuridicaResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## buscar2

> ObsJuridicaResponse buscar2(agendamentoId)

Busca observação jurídica de um atendimento

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.ObservacaoJuridicaApi();
let agendamentoId = "agendamentoId_example"; // String | 
apiInstance.buscar2(agendamentoId).then((data) => {
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

[**ObsJuridicaResponse**](ObsJuridicaResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## criar1

> ObsJuridicaResponse criar1(agendamentoId, obsJuridicaRequest)

Cria observação jurídica — somente NPJ, status deve ser JURIDICO

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.ObservacaoJuridicaApi();
let agendamentoId = "agendamentoId_example"; // String | 
let obsJuridicaRequest = new SalaLilsApi.ObsJuridicaRequest(); // ObsJuridicaRequest | 
apiInstance.criar1(agendamentoId, obsJuridicaRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agendamentoId** | **String**|  | 
 **obsJuridicaRequest** | [**ObsJuridicaRequest**](ObsJuridicaRequest.md)|  | 

### Return type

[**ObsJuridicaResponse**](ObsJuridicaResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

