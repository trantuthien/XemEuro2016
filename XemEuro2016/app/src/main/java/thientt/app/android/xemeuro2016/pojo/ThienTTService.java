package thientt.app.android.xemeuro2016.pojo;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ThienTTService {
    public static <S> S createService(Class<S> serviceClass, String baseURL) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(new StringConverter())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
}
