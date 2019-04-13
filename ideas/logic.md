Получаем delta, originState, gameMap, clientMessages, возвращаем StateDifference

1) Создаём MutableGameObjects из originState
2) Обрабатываем все clientMessages
3) Обрабатываем движение объектов и коллизии
4) Обрабатываем всякие действия Entity (атака, заклинания, эффекты)
5) Возвращаем mutableGameObjects.findStateDifference(originState)