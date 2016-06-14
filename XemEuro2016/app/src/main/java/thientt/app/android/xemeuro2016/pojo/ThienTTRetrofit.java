package thientt.app.android.xemeuro2016.pojo;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ThienTTRetrofit {

//    @FormUrlEncoded
//    @POST("/service/event")
//    Call<String> createGamePlay(@Field("gameplay") String gameplay);

    @GET("/service/linkbongda/all")
    Call<String> getLinkServer();

//    @FormUrlEncoded
//    @POST("/service/like")
//    Call<String> likeEvent(@Field("id") int id, @Field("like") int like);


}

