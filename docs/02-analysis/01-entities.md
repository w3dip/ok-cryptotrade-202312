## Основные сущности приложения CryptoXchange и их взаимосвязи:

### Сущность User

- id (уникальный идентификатор)
- nickname (Пользовательское имя)
- email (электронная почта, указанная при регистрации)
- agreements (список счетов (кошельков))

**Пользователь (аккаунт)**:
Представляет собой зарегистрированного пользователя биржи.
Содержит информацию о пользователе, такую как имя, электронная почта, список счетов (кошельков).

### Сущность Agreement

- id (Уникальный адрес счета)
- name (пользовательское название счета)
- assets (доступные активы)

**Портфель (счет, кошелек)**:
Содержит информацию о текущих активах пользователя, таких как балансы криптовалют и доступные средства.
Обновляется в результате выполнения ордеров по покупке или продаже криптовалюты.

### Сущность Asset

- id (уникальный идентификатор)
- code (уникальный международный идентификатор актива (код))
- name (Название актива)
- quantity (Объем в портфеле)

**Актив (криптовалюта, фиатная валюта)**:
Содержит информацию о торговом инструменте в портфеле

### Сущность Order

- id (уникальный идентификатор заявки)
- userId (идентификатор клиента)
- operationType (тип операции - покупка или продажа)
- secCode (код актива)
- agreementNumber (номер счета, по которому происходит операция)
- quantity (количество)
- price (цена)

**Ордер**:
Представляет собой запрос на покупку или продажу криптовалюты по определенной цене и объему.
Может быть исполнен или отменен.

### Сущность Quote

- code (уникальный международный идентификатор актива (код))
- bid (Лучшая цена покупки)
- offer (Лучшая цена продажи)
- rate (Курс к USD)

**Котировки**:
Содержат информацию о текущих котировках и изменениях цен на криптовалюты.
Предоставляют пользователю актуальные данные для принятия решений о торговле.

Взаимосвязи между сущностями включают в себя создание пользователем ордеров, исполнение ордеров, обновление портфеля
после этого, а также отображение актуальных котировок и графиков для помощи в принятии решений о торговле.

## Функции (эндпониты)

1. CRUDS (create, read, update, delete, search) для портфелей (Agreement)
2. CRUDS (create, read, update, delete, search) для активов (Asset)
3. CRUDS (create, read, update, delete, search) для ордеров (заявок) (Order)
4. CRUDS (create, read, update, delete, search) для котировок (Quote)
