# SalaLilsApi.RelatoriosApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**atendimentos**](RelatoriosApi.md#atendimentos) | **GET** /relatorios/atendimentos | Lista detalhada de atendimentos no período
[**kpis**](RelatoriosApi.md#kpis) | **GET** /relatorios/kpis | KPIS gerais - total de atendimentos, por status e por dia
[**volumeDiario**](RelatoriosApi.md#volumeDiario) | **GET** /relatorios/volume-diario | Volume de atendimentos agrupado por dia



## atendimentos

> [AtendimentoRelatorioResponse] atendimentos(opts)

Lista detalhada de atendimentos no período

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.RelatoriosApi();
let opts = {
  'dataInico': new Date("2013-10-20"), // Date | 
  'dataFim': new Date("2013-10-20"), // Date | 
  'status': "status_example" // String | 
};
apiInstance.atendimentos(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dataInico** | **Date**|  | [optional] 
 **dataFim** | **Date**|  | [optional] 
 **status** | **String**|  | [optional] 

### Return type

[**[AtendimentoRelatorioResponse]**](AtendimentoRelatorioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## kpis

> KpiResponse kpis(opts)

KPIS gerais - total de atendimentos, por status e por dia

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.RelatoriosApi();
let opts = {
  'dataInicio': new Date("2013-10-20"), // Date | 
  'dataFim': new Date("2013-10-20") // Date | 
};
apiInstance.kpis(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dataInicio** | **Date**|  | [optional] 
 **dataFim** | **Date**|  | [optional] 

### Return type

[**KpiResponse**](KpiResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## volumeDiario

> [VolumeDiarioResponse] volumeDiario(opts)

Volume de atendimentos agrupado por dia

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.RelatoriosApi();
let opts = {
  'dataInico': new Date("2013-10-20"), // Date | 
  'dataFim': new Date("2013-10-20") // Date | 
};
apiInstance.volumeDiario(opts).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dataInico** | **Date**|  | [optional] 
 **dataFim** | **Date**|  | [optional] 

### Return type

[**[VolumeDiarioResponse]**](VolumeDiarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

