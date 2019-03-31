package com.mirage.utils.maps

/**
 * Неизменяемый объект карты
 */
interface GameObject {
        /**
         * Название объекта. Используется, например, для поиска объектов по названию.
         */
        val name: String?
        /**
         * Название шаблона объекта.
         */
        val template: String?
        /**
         * Координата x объекта в тайлах
         */
        val x: Float
        /**
         * Координата y объекта в тайлах
         */
        val y: Float
        /**
         * Ширина объекта в тайлах
         */
        val width: Float
        /**
         * Высота объекта в тайлах
         */
        val height: Float
        /**
         * Состояние объекта.
         * Просто кастомная строка, которую можно использовать для хранения какой-нибудь информации,
         * например, в скриптах.
         */
        val state: String?
        /**
         * Является ли объект твердым телом (свойство используется при проверке коллизий)
         */
        val isRigid: Boolean
        /**
         * Скрипты, которые связаны с объектом и вызываются при определённых условиях
         */
        val scripts: Map<String, String>?
}