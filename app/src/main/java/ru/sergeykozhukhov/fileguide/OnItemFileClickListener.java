package ru.sergeykozhukhov.fileguide;

import androidx.annotation.NonNull;

import java.io.File;


/**
 * Интерфейс для обработки нажатий на элементы RecyclerView c файлами
 */
public interface OnItemFileClickListener {

    /**
     * Обработки нажатия в соответствии с параметром
     * @param file - следующая директория
     */
    void onItemClick(@NonNull File file);
}
