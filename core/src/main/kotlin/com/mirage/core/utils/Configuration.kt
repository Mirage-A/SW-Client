package com.mirage.core.utils

const val COMPLETED_QUEST_PHASE = 1000000

const val LOG_ALL_MESSAGES = true

const val OUTER_DLMTR = '\n' //Символ, который должен вставляться в конец каждого сериализованного сообщения
// Этот символ лучше не менять - ClientMessageInputStream читает сообщения построчно
const val INNER_DLMTR = '@'//'φ' //Символ, который должен вставляться внутри сериализованного сообщения для разделения элементов
const val MAP_OBJ_DLMTR = '|'//'ξ' //Символ, по которому разделяются элементы объекта
const val PROPS_DLMTR = '&'//'ψ' //Символ, по которому разделяются properties

const val SERVER_PORT = 48671 //Порт, на котором работает сервер
const val SERVER_ADDRESS = "localhost" //Адрес сервера

const val INTERPOLATION_DELAY_MILLIS = 250L //Задержка в отображении состояния сцены

const val MAX_EXTRAPOLATION_INTERVAL = 250L //Максимальный интервал после получения последнего сообщения, после которого
//экстраполяция не выполняется

const val GAME_LOOP_TICK_INTERVAL = 100L //Интервал между повторениями цикла логики

const val SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL = 50L //Интервал между проверками сервера на наличие новых сообщений

@Volatile
var PLATFORM = "test" //Платформа, на которой запущен клиент

/**
 * Standard screen size.
 * All drawable assets are drawn for a screen of this size.
 * Width/height ratio for all images stays equal for all real screens.
 */
const val DEFAULT_SCREEN_WIDTH = 1920f
const val DEFAULT_SCREEN_HEIGHT = 1080f
/**
 * Size of a tile on virtual screen.
 */
const val TILE_WIDTH = 128f
const val TILE_HEIGHT = 64f

/** Difference between y-coordinate of a player's entity on virtual screen and y-coordinate of a center of screen */
const val DELTA_CENTER_Y = 64f