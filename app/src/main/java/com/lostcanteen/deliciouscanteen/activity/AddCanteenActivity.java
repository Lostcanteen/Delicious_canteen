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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.DBConnection;
import com.lostcanteen.deliciouscanteen.FTP;
import com.lostcanteen.deliciouscanteen.FlowLayout;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.TagItem;
import com.lostcanteen.deliciouscanteen.WebTrans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

//从前页面接受adminid，用于certain按钮点击创建
//certain点击进入下一页面编写
public class AddCanteenActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private Toolbar toolbar;
    private ImageView picture;
    private Uri imageUri;
    private EditText canteenName;
    private EditText location;
    private EditText time;
    private Switch bookon;

    FlowLayout mTagLayout;
    private EditText inputLabel;
    private Button btnSure;
    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();
    private String[] spot = {"包厢预订","生日宴会订做"}; //初始场景
    ArrayList<String>  list = new ArrayList<String>();

    private String basicImagePath = "http://canteen-canteen.stor.sinaapp.com/";
    private String newImagePath ="";
    private File file; // 图片上传到网络，重命名
    private int adminid = 1; //从前页面接受adminid，用于certain按钮点击创建

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_canteen);

        //标签
        mTagLayout = (FlowLayout) findViewById(R.id.tag_layout);
        inputLabel = (EditText) findViewById(R.id.book);
        btnSure = (Button) findViewById(R.id.add);
        initList();
        initLayout(list);
        initBtnListener();

        toolbar = (Toolbar) findViewById(R.id.canteen_toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        canteenName = (EditText) findViewById(R.id.canteenName);
        location = (EditText) findViewById(R.id.location);
        time = (EditText) findViewById(R.id.time);
        bookon = (Switch) findViewById(R.id.bookon) ;
        Button takePhoto = (Button) findViewById(R.id.photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.album);
        Button certain = (Button) findViewById(R.id.certain);
        picture = (ImageView) findViewById(R.id.imageView);
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
                    imageUri = FileProvider.getUriForFile(AddCanteenActivity.this,
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
                if (ContextCompat.checkSelfPermission(AddCanteenActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddCanteenActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });

        certain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file == null) {
                    Toast.makeText(AddCanteenActivity.this,"Error:请上传图片",Toast.LENGTH_SHORT).show();
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

                            String canteenname = canteenName.getText().toString();
                            String loc = location.getText().toString();
                            String hour = time.getText().toString();
                            Boolean on = bookon.isChecked();
                            String[] spots = (String[]) list.toArray(new String[list.size()]);
                            CanteenDetail tmp = new CanteenDetail(0,newImagePath,canteenname,loc,hour,on,spots,adminid);
                            WebTrans.addCanteen(tmp);
                            finish();

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

    private void initList() {
        for(int i=0;i<spot.length;i++){
            list.add(spot[i]);
        }
    }

    private void initBtnListener() {
        /**
         * 初始化  单击事件：
         */
        btnSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String label = inputLabel.getText().toString().trim();

                String[] newStr = new String[mTagLayout.getChildCount()];

                /**
                 * 获取  子view的数量   并添加进去
                 */
                if(label!=null&&!label.equals("")){
                    for(int m = 0;m < mTagLayout.getChildCount()-1;m++){
                        newStr[m] =((TextView)mTagLayout.getChildAt(m).
                                findViewById(R.id.text)).getText().toString();//根据  当前   位置查找到 当前    textView中标签  内容
                    }
                    list.add(label);
                    initLayout(list);
                    inputLabel.setText("");
                }
            }
        });
    }


    private void initLayout(final ArrayList<String> arr) {

        mTagLayout.removeAllViewsInLayout();
        //mTagLayout.removeAllViews();
        /**
         * 创建 textView数组
         */
        final TextView[] textViews = new TextView[arr.size()];
        final TextView[] icons = new TextView[arr.size()];

        for (int i = 0; i < arr.size(); i++) {

            final int pos = i;

            final View view = (View) LayoutInflater.from(AddCanteenActivity.this).inflate(R.layout.text_view, mTagLayout, false);

            final TextView text = (TextView) view.findViewById(R.id.text);  //查找  到当前     textView
            final TextView icon = (TextView) view.findViewById(R.id.delete_icon);  //查找  到当前  删除小图标

            // 将     已有标签设置成      可选标签
            text.setText(list.get(i));
            /**
             * 将当前  textView  赋值给    textView数组
             */
            textViews[i] = text;
            icons[i] = icon;

            //设置    单击事件：
            icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //遍历  图标  删除 当前  被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){  //获取   当前  点击删除图标的位置：
                            mTagLayout.removeViewAt(j);
                            list.remove(j);
                            initLayout(list);
                        }
                    }
                }
            });

            text.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    text.setActivated(!text.isActivated()); // true是激活的

                    if (text.isActivated()) {
                        boolean bResult = doAddText(list.get(pos), false, pos);
                        text.setActivated(bResult);
                        //遍历   数据    将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){//非当前  textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }

                    /**
                     * 遍历  textView  满足   已经被选中     并且不是   当前对象的textView   则置为  不选
                     */
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){//非当前  textView
                            textViews[j].setActivated(false); // true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });

            mTagLayout.addView(view);
        }
        //mTagLayout.removeAllViewsInLayout();
        //mTagLayout.addView(inputLabel);
    }

    // 标签索引文本
    protected int idxTextTag(String text) {
        int mTagCnt = mAddTags.size(); // 添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    private boolean doAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTags.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTags.size(); // 添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTags.add(item);
        tagCnt++;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
