package kazukisaima.kithub.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import io.realm.RealmResults
import kazukisaima.kithub.R
import kazukisaima.kithub.model.realm.Repository
import org.jetbrains.anko.find

/**
 * Created by Kazuki Saima on 15/12/27.
 * All rights reserved by Vapes Inc.
 */

class RepositoryAdapter(
    val context: Context,
    var data: RealmResults<Repository>
): BaseAdapter() {

    val layoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return data.count()
    }

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = getItem(p)
        val view = convertView ?: layoutInflater.inflate(R.layout.list_repository, parent, false)

        val nameTextView: TextView = view.find(R.id.nameTextView)
        val starTextView: TextView = view.find(R.id.starTextView)

        nameTextView.text = item.name
        starTextView.text = "star:${item.stargazersCount}"

        return view
    }

    override fun getItem(p: Int): Repository {
        return data[p]
    }

    override fun getItemId(p: Int): Long {
        return data[p].id.toLong()
    }
}