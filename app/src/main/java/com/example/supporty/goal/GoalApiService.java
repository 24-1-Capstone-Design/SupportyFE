package com.example.supporty.goal;

import com.example.supporty.goal.GoalRes;
import com.example.supporty.goal.GoalData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;

public interface GoalApiService {
    @POST("/goal/:userId")
    Call<GoalRes> goalPostRequest(@Body GoalData data);

    @GET("/goal/:userId")
    Call<GoalRes> goalGetRequest(@Body GoalData data);

    @DELETE("/goal/:userId/:goalId")
    Call<GoalRes> goalDeleteRequest(@Body GoalData data);


    @PATCH("/goal/:userId/:goalId")
    Call<GoalRes> goalPatchRequest(@Body GoalData data);


}
