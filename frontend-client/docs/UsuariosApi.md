# SalaLilsApi.UsuariosApi

All URIs are relative to *http://localhost:8080/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**atualizar**](UsuariosApi.md#atualizar) | **PUT** /usuarios/{id} | Atualiza dados e perfil de um usuário
[**buscar**](UsuariosApi.md#buscar) | **GET** /usuarios/{id} | Retorna dados completos de um usuário
[**criar4**](UsuariosApi.md#criar4) | **POST** /usuarios | Cria novo usuário interno
[**desativar**](UsuariosApi.md#desativar) | **PATCH** /usuarios/{id}/desativar | Desativa o acesso do usuário (soft delete)
[**listar**](UsuariosApi.md#listar) | **GET** /usuarios | Lista de todos os colaboradores
[**reativar**](UsuariosApi.md#reativar) | **PATCH** /usuarios/{id}/reativar | Reativa um usuário previamente desativado



## atualizar

> UsuarioResponse atualizar(id, updateUsuarioRequest)

Atualiza dados e perfil de um usuário

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.UsuariosApi();
let id = "id_example"; // String | 
let updateUsuarioRequest = new SalaLilsApi.UpdateUsuarioRequest(); // UpdateUsuarioRequest | 
apiInstance.atualizar(id, updateUsuarioRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 
 **updateUsuarioRequest** | [**UpdateUsuarioRequest**](UpdateUsuarioRequest.md)|  | 

### Return type

[**UsuarioResponse**](UsuarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## buscar

> UsuarioResponse buscar(id)

Retorna dados completos de um usuário

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.UsuariosApi();
let id = "id_example"; // String | 
apiInstance.buscar(id).then((data) => {
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

[**UsuarioResponse**](UsuarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## criar4

> UsuarioResponse criar4(createUsuarioRequest)

Cria novo usuário interno

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.UsuariosApi();
let createUsuarioRequest = new SalaLilsApi.CreateUsuarioRequest(); // CreateUsuarioRequest | 
apiInstance.criar4(createUsuarioRequest).then((data) => {
  console.log('API called successfully. Returned data: ' + data);
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createUsuarioRequest** | [**CreateUsuarioRequest**](CreateUsuarioRequest.md)|  | 

### Return type

[**UsuarioResponse**](UsuarioResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


## desativar

> desativar(id)

Desativa o acesso do usuário (soft delete)

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.UsuariosApi();
let id = "id_example"; // String | 
apiInstance.desativar(id).then(() => {
  console.log('API called successfully.');
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

null (empty response body)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## listar

> [UsuarioSummaryResponse] listar(opts)

Lista de todos os colaboradores

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.UsuariosApi();
let opts = {
  'busca': "busca_example" // String | 
};
apiInstance.listar(opts).then((data) => {
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

[**[UsuarioSummaryResponse]**](UsuarioSummaryResponse.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


## reativar

> reativar(id)

Reativa um usuário previamente desativado

### Example

```javascript
import SalaLilsApi from 'sala_lils_api';
let defaultClient = SalaLilsApi.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: BearerAuth
let BearerAuth = defaultClient.authentications['BearerAuth'];
BearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new SalaLilsApi.UsuariosApi();
let id = "id_example"; // String | 
apiInstance.reativar(id).then(() => {
  console.log('API called successfully.');
}, (error) => {
  console.error(error);
});

```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | 

### Return type

null (empty response body)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

