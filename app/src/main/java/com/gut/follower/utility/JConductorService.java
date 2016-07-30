package com.gut.follower.utility;


import com.gut.follower.model.Location;
import com.gut.follower.model.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 */
public interface JConductorService {

    @GET("rest/track")
    Call<List<Track>> getTracks();

    @POST("/rest/track")
    Call<Track> postTrack(@Body Location location);

    @POST("/rest/track/{id}")
    Call<Track> postLocation( @Path("id") String id,
                              @Body Location location,
                              @Query("finished") boolean finished);
}
