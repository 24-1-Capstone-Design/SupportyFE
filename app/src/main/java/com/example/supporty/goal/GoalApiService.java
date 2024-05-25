package com.example.supporty.goal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GoalApiService {
    @POST("/goal/{userId}")
    Call<GoalRes> goalPostRequest(@Path("userId") String userId, @Body GoalData data);

    @GET("/goal/{userId}")
    Call<List<GoalRes>> goalGetRequest(@Path("userId") String userId);

    @DELETE("/goal/{userId}/{goalId}")
    Call<GoalRes> goalDeleteRequest(@Path("userId") String userId, @Path("goalId") int goalId);

    @PATCH("/goal/{userId}/{goalId}")
    Call<GoalRes> goalPatchRequest(@Path("userId") String userId, @Path("goalId") int goalId, @Body GoalData data);
}