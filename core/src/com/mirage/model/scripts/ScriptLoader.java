package com.mirage.model.scripts;

import com.badlogic.gdx.Gdx;
import com.mirage.controller.Platform;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class ScriptLoader {

    private static Map<String, IScript> scriptCache = new TreeMap<>();

    /**
     * Загружает скрипт python по заданному относительному пути из папки assets
     */
    public static IScript load(String path) throws IOException {
        if (!scriptCache.containsKey(path)) {
            PythonInterpreter interpreter = new PythonInterpreter();
            interpreter.exec(readUsingBufferedReader(Gdx.files.internal(Platform.INSTANCE.getASSETS_PATH() + "scripts/" + path).reader()));
            PyObject pyObject = interpreter.get("Script");
            PyObject buildingObject = pyObject.__call__();
            scriptCache.put(path, (IScript) buildingObject.__tojava__(IScript.class));
        }
        return scriptCache.get(path);
    }

    /**
     * Считываем содержимое файла в String с помощью BufferedReader
     */
    private static String readUsingBufferedReader(Reader rdr) throws IOException {
        BufferedReader reader = new BufferedReader(rdr);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    /**
     * Очищает кэш с уже загруженными скриптами
     */
    public static void clearCache() {
        scriptCache.clear();
    }
}
