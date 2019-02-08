package com.mirage.model.scripts;

import com.badlogic.gdx.Gdx;
import com.mirage.controller.Platform;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.*;

public class ScriptLoader {

    /**
     * Загружает скрипт python по заданному относительному пути из папки assets
     */
    public static IScript load(String path) throws IOException {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec(readUsingBufferedReader(Gdx.files.internal(Platform.INSTANCE.getASSETS_PATH() + path).reader()));
        PyObject pyObject = interpreter.get("Script");
        PyObject buildingObject = pyObject.__call__();
        return (IScript) buildingObject.__tojava__(IScript.class);
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
}
