package Interface

import Models.Movie
import retrofit2.http.GET
import retrofit2.Call

interface IMovie {
    @GET("movies")
    fun getMovies(): Call<List<Movie>>
}