package com.mirage.utils

import com.mirage.utils.datastructures.Point

const val OUTER_DLMTR = "\n" //Символ, который должен вставляться в конец каждого сериализованного сообщения
                            // Этот символ лучше не менять - ClientMessageInputStream читает сообщения построчно
const val INNER_DLMTR = 'φ' //Символ, который должен вставляться внутри сериализованного сообщения для разделения элементов
const val MAP_OBJ_DLMTR = 'ξ' //Символ, по которому разделяются элементы объекта
const val PROPS_DLMTR = 'ψ' //Символ, по которому разделяются properties

const val SERVER_PORT = 48671 //Порт, на котором работает сервер
const val SERVER_ADDRESS = "localhost" //Адрес сервера

const val INTERPOLATION_DELAY_MILLIS = 250L //Задержка в отображении состояния сцены

const val MAX_EXTRAPOLATION_INTERVAL = 250L //Максимальный интервал после получения последнего сообщения, после которого
                                            //экстраполяция не выполняется

const val GAME_LOOP_TICK_INTERVAL = 100L //Интервал между повторениями цикла логики

const val CONNECTION_MESSAGE_BUFFER_UPDATE_INTERVAL = 50L //Интервал между проверками клиента на наличие новых сообщений
const val SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL = 50L //Интервал между проверками сервера на наличие новых сообщений

const val DESKTOP_FULL_SCREEN = true //Полноэкранный режим для десктопа

val DEFAULT_MAP_POINT = Point(0f, 0f) //Точка карты "по-умолчанию", куда попадают объекты в любой непонятной ситуации

const val DEBUG_MODE = true //Режим дебага

const val SHOW_INVISIBLE_OBJECTS_MODE = false //Режим отображения невидимых предметов //TODO Забагованный

var PLATFORM = "" //Платформа, на которой запущен клиент

const val TILE_HEIGHT = 64f //Высота тайла в пикселях