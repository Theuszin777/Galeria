package magnago.matheus.galeria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;
    static int RESULT_TAKE_PICTURE = 1;
    String currentPhotoPath;
    static int RESULT_REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_main);
        //Acessar e ler o diretório das pictures.
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();
        // Adiciona na lista de fotos.
        for (int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }

        // Criação do MainAdapter e definido no RecycleView.
        mainAdapter = new MainAdapter(MainActivity.this, photos);
        RecyclerView rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setAdapter(mainAdapter);
        // Calculam o número máximo da coluna de fotos que cabem na tela do celular.
        float w = getResources().getDimension(R.dimen.itemWidth);
        int numberOfColumns = Utils.calculateNoOfColumns(MainActivity.this, w);
        // Configuram o RecycleView para exibir as fotos em GRID.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
        rvGallery.setLayoutManager(gridLayoutManager);

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        checkForPermissions(permissions);

    }
    
    public boolean onCreateOptionsMenu (Menu menu) {

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;

    }

    // Definição do que ocorre quando o usuário clicar na TollBar.
    // Câmera
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.opCamera:
                dispatchTakePictureIntent();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }
    // Chama o app de câmera.
    private void dispatchTakePictureIntent() {
        // Criação do arquivo vazio dentro da pasta Pictures.
        File f = null;
        try {
            f = createImageFile();
        }
        // Caso não seja efetuado a criação.
        catch (IOException e) {
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }
        // Guarda o local do arquivo de foto que está sendo manipulado no momento.
        currentPhotoPath = f.getAbsolutePath();

        if (f != null) {
            // Gera um endereço URI.
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "trindade.daniel.galeria.fileprovider", f);
            //  Intenção para acionar a app de câmera.
            Intent i = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            // URI passado para a app de câmera.
            i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
            // Se inicia a app "auxiliar" de câmera.
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }

    // Guarda a imagem, juntamento com data e hora para a criação de diferentes nomes de arquivo "automaticamente".
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);
        return f;
    }

    // Após a app de câmera retornar o valor (foto).
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==  RESULT_TAKE_PICTURE) {
            // Se a foto foi tirada na hora, tal foto é adicionada na lista de fotos.
            if (resultCode == Activity.RESULT_OK) {
                photos.add(currentPhotoPath);
            // Aviso para o MainAdapter sobre a adição de nova foto e consequêntemente o RecycleView também é avisado.
            mainAdapter.notifyItemInserted(photos.size()-1);
            }
        // Caso a foto não tenha sido tirada, o arquivo "vazio" que iria conter a foto, é excluído.
        else {
        File f = new File(currentPhotoPath);
            f.delete();
        }
        }
    }

    // Manda para PhotoActivity o caminho da foto selecionada.
    public void startPhotoActivity(String photoPath) {

        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("Photo_path", photoPath);

        startActivity(i);

    }

    // Recebe uma lista de permissões como entrada.
    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();
        // Verificação de permissões.
        for (String permission : permissions) {
            // Caso a permissão não seja concedida, tal permissão vai para lista de não concedidas.
            if (!hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }
        // Solicitação de permissões que ainda não foram  -- checkSelfPermission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsNotGranted.size() > 0) {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
    }

    // Verifica se tal permissão foi concedida ou não.
    private boolean hasPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    // Verifica se as permissões são necessárias para o funcionamento do APP e se forem, é exibido uma mensagem ao usuário sobre.
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        if (requestCode == RESULT_REQUEST_PERMISSION) {
            for(String permission : permissions) {
                if(!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        if (permissionsRejected.size() > 0) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(MainActivity.this).setMessage("Para usar essa app é preciso conceder essas permissões").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                        }
                    });
                }
            }
        }
    }

}

