# SalaLilsApi.PDFApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**download**](PDFApi.md#download) | **GET** /pdf/{agendamentoId} | Gera ou baixa o PDF consolidade do atendimento



## download

> Blob download(agendamentoId)

Gera ou baixa o PDF consolidade do atendimento

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.PDFApi();
let agendamentoId = "agendamentoId_example"; // String | 
apiInstance.download(agendamentoId).then((data) => {
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

**Blob**

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

