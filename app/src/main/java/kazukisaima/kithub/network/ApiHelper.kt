package kazukisaima.kithub.network

import kazukisaima.kithub.model.moshi.SearchRepositoryResponse
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Created by Kazuki Saima on 15/12/27.
 * All rights reserved by Vapes Inc.
 */

class ApiHelper {

    fun searchRepository(query: String): Observable<SearchRepositoryResponse> {
        return ApiClient().getGitHubApiService().searchRepositories(query)
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext { Observable.empty() }
    }
}