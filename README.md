# 云尚小卖部手持终端接口文档

## 维护记录

```java
/**
*版本：V1.0.1
*日期：2019-04-04 10：28
*维护人：week
*维护记录：
*setPrint（上传打印单）增加备注字段
*/

/**
*版本：V1.0.0
*日期：2019-03-30 22：28
*维护人：week
*维护记录：
*勘误
*/

/**
*版本：V1.0.0
*日期：2019-03-29 19：30
*维护人：week
*维护记录：
*拟定接口方案
*/
```

## 接口方案

1.用账号密码换取token，token有效期为2小时。

2.其他所有接口 必须带上token。

3.请求内容为json字符串，key为data，既data=>json。

## 接口列表

| 接口名               | 描述                                |
| -------------------- | ----------------------------------- |
| login                | 登录接口                            |
| getGoodsByBarcode    | 根据条码获取商品                    |
| getShop              | 获取门店列表                        |
| setPrint             | 上传打印单                          |
| getPrintOrderList    | 获取token对应的用户上传的所有打印单 |
| getPrintOrderDetails | 获取打印单的详情                    |

## 错误码列表

| 错误码 | 英文描述                | 中文描述     |
| ------ | ----------------------- | ------------ |
| 200    | success                 | 调用成功     |
| 400    | account password error  | 账号密码错误 |
| 401    | invalid token           | token失效    |
| 402    | request parameter error | 请求参数错误 |

## 基本参数列表

| 参数名  | 类型   | 是否必须       | 描述                      |
| ------- | ------ | -------------- | ------------------------- |
| version | String | 是             | 接口版本号，目前固定为1.0 |
| apiName | String | 是             | 接口名                    |
| params  | String | 是             | 参数列表                  |
| token   | String | 是（除了登录） | token                     |

## login(登录接口)

**传入参数**:

| 参数名   | 类型   | 描述   |
| -------- | ------ | ------ |
| userName | String | 用户名 |
| passWord | String | 密码   |

**示例json**:

```json
{
	"version": "1.0",
	"apiName": "login",
	"params": {
		"userName": "week",
		"passWord": "123456"
	}
}
```

**返回参数**

| 参数名  | 类型    | 描述                 |
| ------- | ------- | -------------------- |
| code    | Integer | 状态码。             |
| message | String  | code所对应的英文描述 |
| token   | String  | 返回token            |

**示例json：**

```json
{
	"code": 200,
	"message": "success",
	"token": "asdsadasdjkkjk"
}
```

## getGoodsByBarcode(根据条码获取商品)

**传入参数**:

| 参数名  | 类型   | 是否必须 | 描述   |
| ------- | ------ | -------- | ------ |
| barCode | String | 是       | 条码   |
| shopId  | String | 是       | 门店id |

**示例json**:

```json
{
	"version": "1.0",
	"apiName": "getGoodsByBarcode",
	"params": {
		"barCode": "6912323123"，
        "shopId":"123233"
	},
	"token": "asdasdasd"
}
```

**返回参数**

| 参数名  | 类型    | 描述                 |
| ------- | ------- | -------------------- |
| code    | Integer | 状态码。             |
| message | String  | code所对应的英文描述 |
| data    | object  | 返回的数据           |

**data中所包含的数据：**

| 参数名    | 类型    | 描述   |
| --------- | ------- | ------ |
| goodsName | String  | 商品名 |
| barCode   | String  | 条码   |
| total     | Integer | 库存   |
| shopName  | String  | 门店名 |

**示例json：**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"goodsName": "牙刷",
		"barCode": "123323123",
		"total": 45,
		"shopName": "数码店"
	}
}
```

## getShop(获取门店列表)

**传入参数**:无

**示例json**:

```json
{
	"version": "1.0",
	"apiName": "getGoodsByBarcode",
	"params": {
		
	},
	"token": "asdasdasd"
}
```

**返回参数**

| 参数名  | 类型    | 描述                 |
| ------- | ------- | -------------------- |
| code    | Integer | 状态码。             |
| message | String  | code所对应的英文描述 |
| data    | object  | 返回的数据           |

**data中所包含的数据：**

| 参数名   | 类型 | 描述   |
| -------- | ---- | ------ |
| shopList | List | 商品名 |

**shopList中所包含的数据：**

| 参数名   | 类型   | 描述   |
| -------- | ------ | ------ |
| shopId   | String | 门店Id |
| shopName | string | 门店名 |

**示例json：**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"shopList": [{
				"shopId": "1",
				"shopName": "1号店铺"
			},
			{
				"shopId": "2",
				"shopName": "2号店铺"
			},
			{
				"shopId": "1",
				"shopName": "1号店铺"
			}
		]
	}
}
```

## setPrint(上传打印单)

**传入参数**:

| 参数名     | 类型   | 是否必须 | 描述         |
| ---------- | ------ | -------- | ------------ |
| printOrder | object | 是       | 要打印的单子 |

**printOrder所包含的内容：**

| 参数名    | 类型   | 是否必须 | 描述     |
| --------- | ------ | -------- | -------- |
| shopId    | String | 是       | 门店id   |
| goodsList | List   | 是       | 商品列表 |
| mark      | String | 是       | 备注     |

**goodsList所报含的内容：**

| 参数名    | 类型    | 是否必须 | 描述   |
| --------- | ------- | -------- | ------ |
| goodsName | String  | 是       | 商品名 |
| barCode   | String  | 是       | 条码   |
| total     | Integer | 是       | 数量   |

**示例json:**

```json
{
	"version": "1.0",
	"apiName": "setPrint",
	"params": {
		"printOrder": {
			"shopId": "1",
            "mark":"打印单备注",
			"goodsList": [{
				"goodsName": "1号商品",
				"barcode": "001",
				"total": 11
			}, {
				"goodsName": "1号商品",
				"barcode": "001",
				"total": 11
			}, {
				"goodsName": "2号商品",
				"barcode": "002",
				"total": 12
			}, {
				"goodsName": "3号商品",
				"barcode": "003",
				"total": 13
			}, {
				"goodsName": "4号商品",
				"barcode": "004",
				"total": 14
			}]
		}
	},
	"token": "asdasdasd"
}
```

**返回参数**

| 参数名  | 类型    | 描述                 |
| ------- | ------- | -------------------- |
| code    | Integer | 状态码。             |
| message | String  | code所对应的英文描述 |

**示例json：**

```json
{	
	"code": 200,
	"message": "success"
}
```

## getPrintOrderList(获取历史打印订单)

**传入参数**:

| 参数名    | 类型     | 是否必须 | 描述                     |
| --------- | -------- | -------- | ------------------------ |
| startTime | Datetime | 否       | 开始时间，默认为7天前    |
| endTime   | Datetime | 否       | 结束时间，默认为现在     |
| shopId    | String   | 否       | 不传入获取所有门店打印单 |

**示例json**:

```json
{
	"version": "1.0",
	"apiName": "getPrintOrderList",
	"params": {
	},
	"token": "asdasdasd"
}
```

**返回参数**

| 参数名  | 类型    | 描述                 |
| ------- | ------- | -------------------- |
| code    | Integer | 状态码。             |
| message | String  | code所对应的英文描述 |
| data    | object  | 返回的数据           |

**data中所包含的数据：**

| 参数名         | 类型   | 描述       |
| -------------- | ------ | ---------- |
| printOrderList | List<> | 打印单列表 |

**printOrderList中所包含的数据：**

| 参数名       | 类型     | 描述                                  |
| ------------ | -------- | ------------------------------------- |
| shopId       | String   | 门店Id                                |
| shopName     | string   | 门店名                                |
| printOrderId | Integer  | 打印单的ID                            |
| status       | Integer  | 打印单当前状态，0未打印，1打印，2错误 |
| createTime   | DateTime | 创建时间                              |

**示例json：**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"printOrderList": [{
			"shopId": "1",
			"shopName": "1号门店",
			"printOrderId": 12,
			"status": 0,
			"createTime": "2019-03-29 03:00:15"
		}, {
			"shopId": "2",
			"shopName": "2号门店",
			"printOrderId": 122,
			"status": 0,
			"createTime": "2019-03-29 03:10:15"
		}, {
			"shopId": "3",
			"shopName": "3号门店",
			"printOrderId": 312,
			"status": 1,
			"createTime": "2019-03-29 03:20:15"
		}]
	}
}
```

## getPrintOrderDetails(获取打印订单详情)

**传入参数**:

| 参数名       | 类型    | 是否必须 | 描述     |
| ------------ | ------- | -------- | -------- |
| printOrderId | Integer | 是       | 打印单ID |

**示例json**:

```json
{
	"version": "1.0",
	"apiName": "getPrintOrderDetails",
	"params": {
		"printOrderId":123
	},
	"token": "asdasdasd"
}
```

**返回参数**

| 参数名  | 类型    | 描述                 |
| ------- | ------- | -------------------- |
| code    | Integer | 状态码。             |
| message | String  | code所对应的英文描述 |
| data    | object  | 返回的数据           |

**data中所包含的数据：**

| 参数名            | 类型   | 描述       |
| ----------------- | ------ | ---------- |
| printOrderDetails | object | 打印单详情 |

**printOrderDetails中所包含的数据：**

| 参数名       | 类型     | 描述                                  |
| ------------ | -------- | ------------------------------------- |
| shopId       | String   | 门店Id                                |
| shopName     | String   | 门店名                                |
| printOrderId | Integer  | 打印单的ID                            |
| status       | Integer  | 打印单当前状态，0未打印，1打印，2错误 |
| createTime   | DateTime | 创建时间                              |
| returnCode   | String   | 飞蛾打印机回执单号                    |
| printTime    | DateTime | 打印时间                              |
| goodsList    | List     | 商品列表                              |

**goodsList里所包含的数据：**

| 参数名    | 类型    | 是否必须 | 描述   |
| --------- | ------- | -------- | ------ |
| goodsName | String  | 是       | 商品名 |
| barCode   | String  | 是       | 条码   |
| total     | Integer | 是       | 数量   |

**示例json：**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"printOrderDetails": {
			"shopId": "1",
			"shopName": "1号门店",
			"printOrderId": 12,
			"status": 0,
			"createTime": "2019-03-29 03:00:15",
			"returnCode": "asdsd",
			"printTime": "2019-03-29 03:00:16",
			"goodsList": [{
				"goodsName": "1号商品",
				"barcode": "001",
				"total": 11
			}, {
				"goodsName": "1号商品",
				"barcode": "001",
				"total": 11
			}, {
				"goodsName": "2号商品",
				"barcode": "002",
				"total": 12
			}, {
				"goodsName": "3号商品",
				"barcode": "003",
				"total": 13
			}, {
				"goodsName": "4号商品",
				"barcode": "004",
				"total": 14
			}]
		}
	}
}
```


