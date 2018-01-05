package de.vierheller.todocalendar.view.main.projects


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.view.dialogs.MyProjectsDialog
import de.vierheller.todocalendar.viewmodel.ProjectsPragmentViewModel
import kotlinx.android.synthetic.main.fragment_projects.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * A simple [Fragment] subclass.
 */
class ProjectsFragment : Fragment() {
    private lateinit var treeView:AndroidTreeView
    private lateinit var viewModel:ProjectsPragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProjectsPragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_projects, container, false) as FrameLayout

        treeView = AndroidTreeView(activity, viewModel.getViewTree())
        treeView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true)
        treeView.setDefaultViewHolder(CustomProjectsViewHolder(activity).javaClass)
        treeView.setDefaultNodeLongClickListener{ treeNode: TreeNode, value: Any ->
            if(value is ProjectItem){
                Log.d("Tag", value.name)
            }
            true
        }
        view.addView(treeView.view)


        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        fab.onClick {
            val dialog = MyProjectsDialog.getInstance("Hans", "Wurst")
            dialog.setListener{ changed: Boolean, name: String, parent: String ->
                Log.d("TAG", name)
            }
//            dialog.show(activity.supportFragmentManager, "ProjectsDialog")
            viewModel.getTree()
        }
        treeView.expandAll()
        treeView.setUseAutoToggle(false)
    }
}
