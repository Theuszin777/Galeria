package magnago.matheus.galeria;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter {

    // Instância para a classe MainActivity
    MainActivity mainActivity;

    // Lista de Strings - Caminho de uma foto.
    List<String> photos;

    public MainAdapter (MainActivity mainActivity, List<String> photos) {
        this.mainActivity = mainActivity;
        this.photos = photos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Irá inflar o Layout.
        LayoutInflater inflater = LayoutInflater.from(mainActivity);

        // Constrói uma view baseado nas regras do arquivo e em seguida o armazena.
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);

    }

    // Preenche o Image View com a foto correspondente.
    // Possível ERROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // Obtenção das dimensões -> Width = largura
        ImageView imPhoto = holder.itemView.findViewById(R.id.imItem);
            int w = (int) mainActivity.getResources().getDimension(R.dimen.itemWidth);
        // Obtenção das dimensões -> height = altura
        mainActivity.getResources().getDimension(R.dimen.itemWidth);
            int h = (int) mainActivity.getResources().getDimension(R.dimen.itemHeight);
        //Escalonamento da imagem para casar com os tamanhos definidos na Image View.
        mainActivity.getResources().getDimension(R.dimen.itemHeight);
            Bitmap bitmap = Utils.getScaledBitmap(photos.get(position), w, h);

        imPhoto.setImageBitmap(bitmap);
        // Definição do que ocorre ao clicar em uma imagem.
        imPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mainActivity.startPhotoActivity(photos.get(position));
            }
        });
    }


    // Quantos itens existem na lista.
    @Override
    public int getItemCount() {

        return photos.size();

    }

}
