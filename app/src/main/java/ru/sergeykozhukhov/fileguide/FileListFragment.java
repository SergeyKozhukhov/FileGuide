package ru.sergeykozhukhov.fileguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileListFragment extends Fragment {

    /**
     * Код, обозначающий запрос на разрешения доступа к внешнему накопителю
     */
    private static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 1;

    /**
     * Класс, обеспечивающий каркас для перемещения по директориям и получение списка файлов
     */
    private FileGuide fileGuide;

    /**
     * Адаптер файлов, отображаемых в RecyclerView
     */
    private FileAdapter fileAdapter;

    /**
     * RecyclerView для отображения списка файлов
     */
    private RecyclerView listDataRecyclerView;

    /**
     * Кнопка возвращение в родительскую директорию
     */
    private ImageButton backImageButton;

    /**
     * Обработчик нажатия на элемент списка файлов
     */
    private OnItemFileClickListener onItemFileClickListener;

    public static FileListFragment newInstance() {
        return new FileListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_data_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listDataRecyclerView = view.findViewById(R.id.list_data_recycler_view);
        backImageButton = view.findViewById(R.id.back_image_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listDataRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        initListeners();

        fileAdapter = new FileAdapter(onItemFileClickListener);
        listDataRecyclerView.setAdapter(fileAdapter);
        initFileGuide();
    }


    /**
     * Инициализация обработчиков
     */
    private void initListeners(){
         onItemFileClickListener = new OnItemFileClickListener() {
            @Override
            public void onItemClick(@NonNull File file) {
                if (file.isDirectory()) {
                    fileGuide.navigateTo(file);
                    updateFileList();
                }
            }
        };

         backImageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 fileGuide.navigateUp();
                 updateFileList();
             }
         });
    }

    /**
     * Инициализация каркаса для работы с файлами и директориями
     *
     * checkSelfPermission - проверка, доступно ли обозначенное разрешение
     */
    private void initFileGuide() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            fileGuide = new FileGuide(requireContext());
            updateFileList();
        }
        else{
            requestPermissions();
        }
    }

    /**
     * Запрос на разрешения доступа к внешнему хранилищу
     *
     * requestPermissions - запрос на получение разрешения от пользователя
     * requireActivity - activity-host фрагмента
     * new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} - список разрешений из одного запрашиваемого разрешения на SD карту
     * REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE - код для получения результата от пользователя по данному запросу
     */
    private void requestPermissions(){
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE);
    }

    /**
     * CallBack для результата каждого запроса на разрешение
     *
     * @param requestCode - код запроса
     * @param permissions - список запрашиваемых разрешений
     * @param grantResults - результаты предоставления соответствующих резрешений
     *
     * PERMISSION_GRANTE - разрешение предоставленно
     * PERMISSION_DENIED - разрешение отклонено
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initFileGuide();
                updateFileList();
            }
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                requestPermissions();
            }
        }
    }

    /**
     * Обновление адаптера файлов в стоответствии с файлами текущией директории
     */
    private void updateFileList() {
        List<File> files = fileGuide.getFiles();

        if (files == null)
            return;

        fileAdapter.setFiles(files);
        fileAdapter.notifyDataSetChanged();

    }
}
