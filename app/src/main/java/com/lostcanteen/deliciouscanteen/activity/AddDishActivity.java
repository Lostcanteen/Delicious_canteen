package com.lostcanteen.deliciouscanteen.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.FTP;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

// 图片上传到网络，重命名
//添加dish提交问题
public class AddDishActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;

    private Toolbar toolbar;
    private TextView title;
    private Uri imageUri;
    private EditText dishName;
    private EditText price;
    private CheckBox main;
    private CheckBox breakfast;
    private CheckBox lunch;
    private CheckBox dinner;

    private String basicImagePath = "http://canteen-canteen.stor.sinaapp.com/";
    private String newImagePath ="";
    private File file;

    private int canteenId;

    private boolean isAdd = false;
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        canteenId = getIntent().getIntExtra("canteenId",-1);

        dishName = (EditText) findViewById(R.id.dishName);
        price = (EditText) findViewById(R.id.price);
        main = (CheckBox) findViewById(R.id.main);
        breakfast = (CheckBox) findViewById(R.id.breakfast);
        lunch = (CheckBox) findViewById(R.id.lunch);
        dinner = (CheckBox) findViewById(R.id.dinner);
        Button takePhoto = (Button) findViewById(R.id.photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.album);
        Button certain = (Button) findViewById(R.id.certain);
        picture = (ImageView) findViewById(R.id.imageView);

        toolbar = (Toolbar) findViewById(R.id.adddish_toolbar);
        title = (TextView) findViewById(R.id.adddishTitle);
        title.setText("添加菜");
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UUID uuid = UUID.randomUUID();
                long time = System.currentTimeMillis();
                File outputImage = new File(getExternalCacheDir(),uuid+"-"+time+".jpg");
                file = outputImage;
                newImagePath = basicImagePath+file.getName();
                try {
                    if(outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(AddDishActivity.this,
                            "com.lostcanteen.deliciouscanteen",outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddDishActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddDishActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });

        certain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file == null) {
                    Toast.makeText(AddDishActivity.this,"Error:请上传图片",Toast.LENGTH_SHORT).show();
                } else if(dishName.getText().toString().equals("")) {
                    Toast.makeText(AddDishActivity.this,"Error:请输入菜名",Toast.LENGTH_SHORT).show();
                } else if(!Pattern.compile("[\\d]+\\.[\\d]+|[\\d]").matcher(price.getText().toString()).find()) {
                    Toast.makeText(AddDishActivity.this, "Error:请在价格处输入数字", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new FTP().uploadSingleFile(file,"/canteen", new FTP.UploadProgressListener() {
                                    @Override
                                    public void onUploadProgress(String currentStep, long uploadSize, File file) {

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String dishname = dishName.getText().toString();
                            Float p = Float.parseFloat(price.getText().toString());
                            boolean m = main.isChecked();
                            boolean b = breakfast.isChecked();
                            boolean l = lunch.isChecked();
                            boolean d = dinner.isChecked();
                            Dish dish = new Dish(canteenId,0,dishname,newImagePath,p,b,l,d,m);
                            isAdd = WebTrans.addDish(dish);

                            Intent intent = new Intent();
                            intent.putExtra("isAdd",isAdd);

                            AddDishActivity.this.setResult(RESULT_OK,intent);

                            AddDishActivity.this.finish();
                        }

                    }).start();
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);//图片显示比例设置
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    handleImageBeforeKitKat(data);
                }
                break;
            default:
                break;
        }
    }
    /*
         private void handleImageOnKitKat(Intent data) {
            String imagePath = null;
            Uri uri = data.getData();
    //        如果uri是document类型，就通过读取document id进行处理
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
    //            如果Uri的Authority是media格式的话，document id还需要进行解析，然后通过字符串
    //            分割方式取出后半部分才能得到真正的数字id
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads,documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads.public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
    //            如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
    //            如果是file类型，直接获取图片路径
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        file = new File(imagePath);
        newImagePath = basicImagePath+file.getName();
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null ) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
