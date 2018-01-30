package de.vierheller.todocalendar.view.main.projects


import android.arch.lifecycle.Observer
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
import de.vierheller.todocalendar.viewmodel.ProjectsFragmentViewModel
import kotlinx.android.synthetic.main.fragment_projects.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * A simple [Fragment] subclass.
 */
class ProjectsFragment : Fragment() {
    private lateinit var treeView:AndroidTreeView
    private lateinit var viewModel: ProjectsFragmentViewModel

    private lateinit var treeViewView:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProjectsFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_projects, container, false) as FrameLayout

        treeView = AndroidTreeView(activity, TreeNode.root())
        treeView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true)
        treeView.setDefaultViewHolder(CustomProjectsViewHolder(this.activity!!).javaClass)
        treeView.setDefaultNodeLongClickListener{ treeNode: TreeNode, value: Any ->
            if(value is ProjectItem){
                Log.d("Tag", value.name)
            }
            true
        }
        treeViewView = treeView.view
        view.addView(treeViewView)


        return view
    }

    fun createDialog(name:String, parent:Int){
        val dialog = MyProjectsDialog.getInstance(name, parent)
        dialog.setListener{ changed: Boolean, newName: String, parentPosition: Int ->
            Log.d("TAG", "${newName} ${parentPosition}")
            viewModel.addProject(newName, parentPosition)
        }
        dialog.show(activity!!.supportFragmentManager, "ProjectsDialog")
    }

    fun createDialog(){
        createDialog("", -1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab.onClick {
            createDialog()
        }
        viewModel.getTree().observe(this, Observer {
            Log.d("TAG", "tree ${it}")
            treeView.setRoot(it)
            (view as FrameLayout).removeView(treeViewView)
            treeViewView = treeView.getView()
            view.addView(treeViewView)
            treeView.expandAll()
        })

        treeView.expandAll()
        treeView.setUseAutoToggle(false)
    }
}
