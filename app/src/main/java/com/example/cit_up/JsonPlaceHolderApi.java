package com.example.cit_up;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("test")
    Call<List<Post>> getPost();
}
