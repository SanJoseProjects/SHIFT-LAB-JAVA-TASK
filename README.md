#SHOP
##Использованные технологии
1. БД: H2 (in memory)
2. Spring Framework: 6.0.4
3. SpringBoot: 3.0.2
4. MapStruct: 1.5.3Final
5. lombok

##Развёртывание
Данное приложение нужно открыть в IntelliJ IDEA, после чего нужно запустить сборщик gradle.
В gradle прописаны все зависимости и всё необходимое поставится при сборке.
Далее запустить приложение через метод main класса Application.

##API
###Создать товар
POST http://localhost:8081/shop/products 

Req
```json
{
  "serialNumber": "EFJEIFJE-2323FEEF-33R3FFVE",
  "producer": "Lenovo",
  "price": "399.99",
  "quantity": "100",
  "type": "SCREEN",
  "description": "19"
}
```

Ans
Http status 200
```json
{
  "id": "1"
}
```

Http status 400
```json
{
  "code": "1",
  "message": "Серийный номер уже занят"
}
```

Http status 400
```json
{
  "code": "12",
  "message": "Не указан id товара"
}
```

###Редактировать товар
PUT http://localhost:8081/shop/products

Req

```json
{
    "id": "1",
    "serialNumber": "EFJEIFJE-2323FEEF-33R3FFVE",
    "producer": "Lenovo",
    "price": "399.99",
    "quantity": "100",
    "type": "SCREEN",
    "description": "19"
}
```

Ans

Http status 200
```json
{}
```

Http status 400
```json
{
  "code": "12",
  "message": "Не указан id товара"
}
```

Http status 404
```json
{
  "code": "3",
  "message": "Товар не найден"
}
```

###Удаление товара
DELETE http://localhost:8081/shop/products

Req

```json
{
  "id": "1"
}
```

Ans

Http status 200
```json
{}
```

Http status 400
```json
{
  "code": "12",
  "message": "Не указан id товара"
}
```

Http status 404
```json
{
  "code": "3",
  "message": "Товар не найден"
}
```


###Получить список товаров по категории
GET http://localhost:8081/shop/products?type=SCREEN

Ans

Http status 200
```json
[
  {
    "id": 1,
    "serialNumber": "EFJEIFJE-2323FEEF-33R3FFVE",
    "producer": "Lenovo",
    "price": 399.99,
    "quantity": 100,
    "type": "SCREEN",
    "description": "19.5"
  }, ...
]
```

Http status 400
```json
{
  "code": "2",
  "message": "Неверный тип продукта"
}
```

###Получить товар по id
GET http://localhost:8081/shop/product?id=1

Ans

Http status 200
```json
{
    "id": 1,
    "serialNumber": "EFJEIFJE-2323FEEF-33R3FFVE",
    "producer": "Lenovo",
    "price": 399.99,
    "quantity": 100,
    "type": "SCREEN",
    "description": "19.5"
}
```

Http status 404
```json
{
  "code": "3",
  "message": "Товар не найден"
}
```

###Дополнительные ошибки
В запросах POST и PUT также могут вылететь следующие ошибки

Http status 400
```json
{
  "code": "5",
  "message": "Неверный форм-фактор десктопа"
}
```

Http status 400
```json
{
  "code": "6",
  "message": "Неверный размер ноутбука"
}
```

Http status 400
```json
{
  "code": "7",
  "message": "Неверный объём жёсткого диска"
}
```

Http status 400
```json
{
  "code": "8",
  "message": "Неверная диагональ монитора"
}
```

Http status 400
```json
{
  "code": "2",
  "message": "Неверный тип продукта"
}
```

Http status 400
```json
{
  "code": "4",
  "message": "Поле серийного номера пустое"
}
```

Http status 400
```json
{
  "code": "9",
  "message": "Нет поля описания товара"
}
```

Http status 400
```json
{
  "code": "10",
  "message": "Некорректная цена товара"
}
```

Http status 400
```json
{
  "code": "11",
  "message": "Некорректное количество товара на складе"
}
```