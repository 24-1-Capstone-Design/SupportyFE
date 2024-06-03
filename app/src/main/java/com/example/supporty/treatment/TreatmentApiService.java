package com.example.supporty.treatment;

import com.example.supporty.goal.GoalData;
import com.example.supporty.goal.GoalRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TreatmentApiService {
        @POST("/treatment/create")
        Call<TreatmentRes> treatmentPostRequest(@Query("id") String id, @Body TreatmentData data);

        @GET("/treatment")
        Call<List<TreatmentRes>> treatmentGetRequest(@Query("id") String id, @Query("visitDate") String date);

        @PATCH("/treatment/update")
        Call<TreatmentRes> treatmentPatchRequest(@Query("id") String id, @Query("visitId") int visitId, @Body TreatmentData data);

        @DELETE("/treatment/delete")
        Call<Void> treatmentDeleteRequest(@Query("id") String id, @Query("visitId") int visitId);
}
