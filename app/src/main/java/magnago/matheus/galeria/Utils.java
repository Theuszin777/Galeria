package magnago.matheus.galeria;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class Utils {

    // Calcula o números de colunas com um tamanho de foto determinado.
    public static int calculateNumberOfColumns(Context context, float columnWidth) {
        // Captura as medidas.
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;
        // Conta: tamanho total da tela (widthPixels) sobre tamanho de cada item (columnWidth).
        return (int) (screenWidth / columnWidth + (1/2));

    }


    // Define qual o tamanho (WIDTH AND HEIGHT) da foto dentro do app.
    public static Bitmap getScaledBitmap(String imagePath, int w, int h) {
        // Armazena as informações da foto.
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // Só carregar as informações da imagem
        bmOptions.inJustDecodeBounds = true;
        // Decodifica as informações do arquivo e manda para o bmOptions.
        BitmapFactory.decodeFile(imagePath, bmOptions);

        // Tamanho da foto.
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Escalonamento -- Tamanho original da imagem para o que eu quero.
        int scaleFactor = Math.max(photoW/w, photoH/h);

        // Já foi feito o Escalonamento, agr eu quero que carregue a foto.
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);

    }

    public static Bitmap getBitmap(String imagePath) {
        // Armazena as informações da foto.
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // Só carregar as informações da imagem
        bmOptions.inJustDecodeBounds = false;
        // Decodifica as informações do arquivo e manda para o bmOptions.
        return BitmapFactory.decodeFile(imagePath, bmOptions);

    }

}
