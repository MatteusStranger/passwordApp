package ufmt.javatechig.PasswordApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private GridView gridView = null;

    private GridViewAdapter gridAdapter = null;
    private String root = "/storage/emulated/0/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData(root));
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());

                //intent.putExtra("image", Bitmap.createBitmap(item.getImage(),0,0,300,300)); // Reduction dimension of image to show in activity
                intent.putExtra("pathFile", item.getAbsolutpathPath());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ArrayList<String> getPathImages(String root) {
        ArrayList<String> temp = new ArrayList<>();
        File f = new File(root);
        if (!f.exists()) return null;

        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                //if ( file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg")) temp.add(file.getAbsolutePath());
                if (file.getName().endsWith(".jpeg") ||
                        file.getName().endsWith(".jpg") ||
                file.getName().endsWith(".JPG") ||
                file.getName().endsWith(".png"))temp.add(file.getAbsolutePath());
            }
        }
        return temp;
    }

    private Bitmap ImagemEscala(Bitmap bMap, float r) {
        int w = (int) ((int) bMap.getWidth() * r);
        int h = (int) ((int) bMap.getHeight() * r);
        return Bitmap.createScaledBitmap(bMap, w, h, true);   // Diminuir a dimensão
    }

    private ArrayList<ImageItem> getData(String root) {
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        ArrayList<String> imagens = getPathImages(root);
        FileInputStream in;
        BufferedInputStream buf;
        if (imagens != null) {
            for (int i = 0; i < imagens.size(); i++) {
                try {
                    String f = imagens.get(i);
                    in = new FileInputStream(f);
                    buf = new BufferedInputStream(in);
                    Bitmap bMap = BitmapFactory.decodeStream(buf);

                    imageItems.add(new ImageItem(ImagemEscala(bMap, (float) 0.5), "Imagem " + i, f));
                    in.close();
                    buf.close();

                } catch (Exception e) {
                    Log.e("Error reading file", e.toString());
                }
            }
        }
        return imageItems;
    }


    /*
    ArrayList<String> imagens = new ArrayList<>();
    String root = "/sdcard/Download/";
    void getPathImages(String root) {
        File f = new File(root);
        if(f.exists()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    //if ( file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg")) temp.add(file.getAbsolutePath());
                    if (file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg")) imagens.add(file.getAbsolutePath());
                }
            }
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPathImages(root);
        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imagens.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(15, 15, 15, 15);
            } else {
                imageView = (ImageView) convertView;
            }

            FileInputStream in;
            BufferedInputStream buf;

            try {

                in = new FileInputStream(imagens.get(position));
                buf = new BufferedInputStream(in);
                Bitmap bitmap = BitmapFactory.decodeStream(buf);
                in.close();
                buf.close();
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }

            return imageView;
        }
    }
    */

}