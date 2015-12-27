package kazukisaima.kithub.presentation.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ListViewCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.bindView
import io.realm.Realm
import io.realm.RealmChangeListener
import kazukisaima.kithub.R
import kazukisaima.kithub.model.realm.Repository
import kazukisaima.kithub.model.realm.User
import kazukisaima.kithub.network.ApiClient
import kazukisaima.kithub.network.ApiHelper
import kazukisaima.kithub.network.GitHubApiService
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import timber.log.Timber
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val listView: ListViewCompat by bindView(R.id.listView)

    private var realm: Realm by Delegates.notNull()
    private var realmChangeListener: RealmChangeListener by Delegates.notNull()
    private val api: GitHubApiService = ApiClient().getGitHubApiService()

    private var subscription: Subscription by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        realm = Realm.getInstance(this)
        realmChangeListener = RealmChangeListener {
            Timber.d("data changed")
        }
        realm.addChangeListener(realmChangeListener)
    }

    override fun onStart() {
        super.onStart()
        subscription = api.searchRepositories("jquery")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .flatMap { Observable.from(it.items) }
            .map {
                realm.beginTransaction()
                val user = it.owner.run {
                    User(id, name, avatarURLString, urlString)
                }
                realm.copyToRealmOrUpdate(user)

                val repo = it.run {
                    Repository(
                        id, name, fullName, user, private, description, urlString,
                        createDate, updateDate, stargazersCount, watchersCount, score
                    )
                }
                realm.copyToRealmOrUpdate(repo)
                realm.commitTransaction()
                repo
            }
            .subscribe()
    }

    override fun onStop() {
        super.onStop()
        subscription.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.removeChangeListener(realmChangeListener)
        realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId ?: return true
        if (id == R.id.menu_search) {

        }

        return super.onOptionsItemSelected(item)
    }
}
