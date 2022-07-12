package com.example.kaiyum.ui.review;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Review;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.example.kaiyum.ui.login.LoginActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kakao.sdk.user.UserApiClient;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ReviewActivity extends AppCompatActivity {
    Review reviewForUpload = new Review();
    RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);

    // for image upload
    private final int CODE_ALBUM_REQUEST = 201;
    Uri selectedImage;
    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        init();
    }

    private void init() {
        // 로그인 여부 확인 및 회원 id 불러오기
        updateKakaoLogin();

        getRestaurantInfo();
        handleScore();
        handleImageUpload();
        handlePostRequest();
    }

    private void updateKakaoLogin() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                reviewForUpload.setUnid(user.getId());

                Call<JsonObject> call = service.getUser(reviewForUpload.getUnid());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JsonObject user = response.body();

                            TextView userNameTextView = findViewById(R.id.review_userName);
                            userNameTextView.setText(user.get("nickname").getAsString());
                        } catch (Exception e) {
                            goToLogin();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        goToLogin();
                    }
                });

            } else {
                goToLogin();
            }
            return null;
        });
    }

    private void getRestaurantInfo(){
        Intent intent = getIntent();
        TextView restaurantNameTextView = findViewById(R.id.review_restaurantName);
        restaurantNameTextView.setText(intent.getStringExtra("restaurantName"));
        reviewForUpload.setRid(intent.getIntExtra("rid", 0));
    }

    private void goToLogin() {
        // 로그인 풀려있을 시 로그인 페이지로 다시 보내기
        Toast.makeText(getApplicationContext(), "로그인이 해제되었습니다. 다시 로그인 부탁드립니다!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ReviewActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void handleScore() {
        ImageView[] STARS = {
                findViewById(R.id.review_star1),
                findViewById(R.id.review_star2),
                findViewById(R.id.review_star3),
                findViewById(R.id.review_star4),
                findViewById(R.id.review_star5)
        };

        String[] COMMENTS = {
                "별로였어요",
                "그렇게 좋진 않았어요",
                "먹을만 해요",
                "맛있게 먹었어요",
                "완전 추천해요"
        };

        for (int i = 0; i < STARS.length; i++) {
            int currentIndex = i;

            STARS[currentIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 별 클릭 시 평점만큼 별이 채워지도록 하기
                    for (int j = 0; j < STARS.length; j++) {
                        if (j <= currentIndex) {
                            STARS[j].setImageResource(R.drawable.star_on);
                        } else {
                            STARS[j].setImageResource(R.drawable.star_off);
                        }
                    }

                    // 별점에 따라 텍스트 변경
                    TextView comment = findViewById(R.id.review_scoreComment);
                    comment.setText(COMMENTS[currentIndex]);

                    // score 정보 객체에 저장
                    reviewForUpload.setScore(currentIndex + 1);
                }
            });
        }
    }

    private void handleImageUpload() {
        Button imageUploadBtn = findViewById(R.id.review_imageUploadBtn);

        imageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, CODE_ALBUM_REQUEST);
            }
        });
    }

    // 사진 선택 후 이미지 비트맵으로 전환
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView imgStatusTextView = findViewById(R.id.review_imageStatusTextView);

        if (requestCode == CODE_ALBUM_REQUEST && resultCode == RESULT_OK && data != null) {
            imgStatusTextView.setText("사진 업로드 완료!");
            imgStatusTextView.setTextColor(getColor(R.color.green_dark));
            // 받아온 사진 URI를 cursor를 이용하여 PATH 저장
            selectedImage = data.getData();

            Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }else{
            imgStatusTextView.setText("사진을 선택하지 않으셨습니다.");
        }
    }


    private void handlePostRequest(){
        Button uploadBtn = findViewById(R.id.review_uploadReviewBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 작성한 후기를 객체에 저장
                TextView reviewTextView = findViewById(R.id.review_textInput);
                reviewForUpload.setText(reviewTextView.getText().toString());

                // score, text 체크해서 없으면 채우라고 토스트 메세지 띄움
                if(!validCheck()){
                    return;
                }

                Call<JsonObject> call = service.addReview(reviewForUpload);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        handleImagePost(response.body().get("review_id").getAsInt());
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void handleImagePost(int review_id){
        if(mediaPath != null) {
            File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);

            Call<JsonObject> call = service.addReviewImage(fileToUpload, review_id);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "소중한 후기 감사합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "사진 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "소중한 후기 감사합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validCheck(){
        if(reviewForUpload.getRid() == 0 || reviewForUpload.getUnid() == 0 || reviewForUpload.getScore() == 0 || reviewForUpload.getText().equals("")){
            Toast.makeText(getApplicationContext(), "별점, 후기를 작성해주세요", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }
}