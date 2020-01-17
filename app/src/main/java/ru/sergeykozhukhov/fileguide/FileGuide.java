package ru.sergeykozhukhov.fileguide;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс, обеспечивающий каркас для перемещения по директориям и получение списка файлов
 */
public class FileGuide {

    /**
     * currentDirectory - текущая директория
     * File - класс для работы с файлами и директориями
     */
    private File currentDirectory;

    /**
     * rootDirectory - корневая директория
     * Определяет начальную директория, выше которой подниматься запрещено
     */
    private final File rootDirectory;

    /**
     * Конструктор
     *
     * Производится проверка присутстует ли съемный носитель,
     * если присутствует сохраняем путь к нему, в противном случае сохраняем путь к папке приложения     *
     *
     * Environment - поставщик доступа к переменным системы
     * MEDIA_MOUNTED - состояние, определяющее, что карта вставлена и готова к работе
     * Environment.getExternalStorageState() - функция, возвращающая текущее состояние внешнего накопился
     *
     * ContextCompat - класс библиотеки совместимости для работы с функциями контекста
     * getDataDir(...) - путь к директории данных настоящего приложения
     */
    public FileGuide(Context context) {

        File directory;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            directory = Environment.getExternalStorageDirectory();
        }else {
            directory = ContextCompat.getDataDir(context);
        }
        rootDirectory = directory;
        currentDirectory = directory;
    }


    /**
     * Функия, обеспечивающая переход "текущей директории" к заданной в параметре
     * при условии, что это папка и что она лежит внутри rootDirectory
     *
     * @param directory - директория, которую требуется установить как текущую
     * @return true - если операция выполненна успешна, false - если нет
     */
    public boolean navigateTo(File directory){
        if (!directory.isDirectory()){
            return false;
        }
        if (!directory.equals(rootDirectory)&&
            rootDirectory.getAbsolutePath().contains(directory.getAbsolutePath())){
            return false;
        }
        currentDirectory = directory;

        return true;
    }


    /**
     * Функция по возврату currentDirectory на уровень выше
     *
     * @return true - если операция выполненна успешна, false - если нет
     */
    public boolean navigateUp(){
        File fileUp = currentDirectory.getParentFile();
        if (fileUp == null)
            return false;
        return navigateTo(fileUp);
    }


    /**
     * Получение списка файлов (объектов) текущей директории
     *
     * @return список файлов текущей директории
     */
    @Nullable
    public List<File> getFiles(){

        File[] files = currentDirectory.listFiles();
        if(files == null)
            return null;
        return new ArrayList<>(Arrays.asList(files));
    }
}
