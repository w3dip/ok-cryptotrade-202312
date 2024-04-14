Получить список топиков

```shell
docker exec -ti kafka /opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

Отправить сообщение

```shell
docker exec -ti kafka /opt/kafka/bin/kafka-console-producer.sh --topic cryptotrade-order-v1-in --bootstrap-server localhost:9092
```

Каждая строка - одно сообщение. Прервать - Ctrl+Z

Получить сообщения

```shell
docker exec -ti kafka /opt/kafka/bin/kafka-console-consumer.sh --from-beginning --topic cryptotrade-order-v1-in --bootstrap-server localhost:9092 
```

Получить сообщения как consumer1

```shell
docker exec -ti kafka /opt/kafka/bin/kafka-console-consumer.sh --group consumer1 --topic cryptotrade-order-v1-in --bootstrap-server localhost:9092 
```

Отправить сообщение с ключом через двоеточие (key:value)

```shell
docker exec -ti kafka /opt/kafka/bin/kafka-console-producer.sh --topic cryptotrade-order-v1-in --property "parse.key=true" --property "key.separator=:" --bootstrap-server localhost:9092
```
