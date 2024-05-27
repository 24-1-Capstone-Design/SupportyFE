package com.example.supporty.goal;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GoalApiService {
    @POST("/goal/create")
    Call<GoalRes> goalPostRequest(@Query("id") String id, @Body GoalData data);

    @GET("/goal")
    Call<List<GoalRes>> goalGetRequest(@Query("id") String id, @Query("goalDate") String date);

    @PATCH("/goal/update")
    Call<GoalRes> goalPatchRequest(@Query("id") String id, @Query("goalId") int goalId, @Body GoalData data);

    @DELETE("/goal/delete")
    Call<Void> goalDeleteRequest(@Query("id") String id, @Query("goalId") int goalId);
}