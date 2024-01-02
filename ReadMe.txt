docker volume create rabbitmq_data

docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 -v rabbitmq data:/var/lib/rqbbitmq -- restart=unless-stopped rabbit:3.11.0-management
флаги:
    -d - запуск в режиме демона
    --hostname rabbitmq - позволяет задать адрес контейнера, для подключения внутри докера из других контейнеров
    --name rabbitmq - позволяет задать имя контейнера
    - p 5672:5673 - 5672 - порт снаружи докера, 5673 - порт внутри докера
    - v - означает volume позволяет примонтировать внешнее хранилище, volume перед этим должен быть создан,
    после названия через двоеточие указывается путь где будут хранится данные в операционной системе
    для windows
    rabbitmq_data: c:\rabbitmq_data

    --restart=unless-stopped - означает что контейнер будет автоматически подниматься при старте докера,
    независимо от того завершился ли он до этого успешно или упал с ошибками

docker exec -it rabbitmq /bin/bash

внутри контейнера
rabbitmqctl add_user userok mypwd - создать пользователя
rabbitmqctl set_user_tags userok administrator - выдать пользователю права администратора
rabbitmqctl set_permissions -p / userok ".*" ".*" ".*" --доступы на чтение запись и изменение конфигурации