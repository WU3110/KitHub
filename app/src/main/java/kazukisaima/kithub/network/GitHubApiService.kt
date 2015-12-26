package kazukisaima.kithub.network

import retrofit.http.GET
import retrofit.http.Query

/**
 * Created by Kazuki Saima on 15/12/27.
 * All rights reserved by Vapes Inc.
 */

interface GitHubApiService {

    @GET("/search/repositories?sort=stars&order=desc")
    fun searchRepositories(@Query("q") query: String)
}

