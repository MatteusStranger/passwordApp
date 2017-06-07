package ufmt.javatechig.PasswordApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class DetailsActivity extends ActionBarActivity {
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        /*
        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = getIntent().getParcelableExtra("image");
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
        */


        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        path = getIntent().getStringExtra("pathFile");
        FileInputStream in;
        BufferedInputStream buf;

        try {
            ImageView imageView = (ImageView) findViewById(R.id.image);
            in = new FileInputStream(path);
            buf = new BufferedInputStream(in);
            Bitmap bitmap = BitmapFactory.decodeStream(buf);
            in.close();
            buf.close();
            imageView.setImageBitmap(ImagemEscala(bitmap));

        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }

    }

    public void onBackPressed() {
        Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        finish();
        return;
    }
    private Bitmap ImagemEscala(Bitmap bMap){
        if(bMap.getByteCount() >= 262144) return bMap;
        Point tela  = tamTela();
        int w = bMap.getWidth();
        int h = bMap.getHeight();

        Log.i("TAMNHO LAR",""+tela.x);
        Log.i("TAMNHO ALT",""+tela.y);

        if(w > tela.x) w = tela.x;
        if(h > tela.y) h = tela.y;

        return Bitmap.createScaledBitmap(bMap,w,h, true);   // Diminuir a dimensão
    }

    private Point tamTela(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Log.i("Compartilhar", "ok");

            Intent share = new Intent(Intent.ACTION_SEND);
            // setType("image/png"); ou para jpeg: setType("image/jpeg");
            share.setType("image/*");

            // Necessario ter a imagem no diretorio
            File imageFileToShare = new File(path);
            Uri uri = Uri.fromFile(imageFileToShare);
            share.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(share, "Compartilhar imagem"));
            return true;
        }

        if (id == R.id.delete) {

            File fDel = new File(path);
            if(fDel.delete()){
                Toast.makeText(getApplicationContext(), "Imagem deletada com sucesso", Toast.LENGTH_SHORT).show();
                Log.i("Delete status", "Deletado");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Falha ao deletar a imagem", Toast.LENGTH_SHORT).show();
                Log.i("Delete status", "Não deletado");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            Log.i("Delete", "ok");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
