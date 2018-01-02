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

        treeView = AndroidTreeView(activity, createTree())
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
            dialog.show(activity.supportFragmentManager, "ProjectsDialog")
            dialog.setListener{ changed: Boolean, name: String, parent: String ->
                Log.d("TAG", name)
            }
        }
        treeView.expandAll()
        treeView.setUseAutoToggle(false)
    }

    private fun createTree(): TreeNode {
        val root = TreeNode.root()
        val pro1 = TreeNode(ProjectItem("Project1",0))
        val pro1_1 = TreeNode(ProjectItem("Project1-1",1))
        val pro1_2 = TreeNode(ProjectItem("Project1-2",1))
        pro1.addChild(pro1_1)
        pro1.addChild(pro1_2)

        val pro2 = TreeNode(ProjectItem("Project2",0))

        val pro3 = TreeNode(ProjectItem("Project3",0))
        val pro3_1 = TreeNode(ProjectItem("Project3-1",1))
        val pro3_1_1 = TreeNode(ProjectItem("Project3-1-1",2))
        pro3_1.addChild(pro3_1_1)
        pro3.addChild(pro3_1)


        root.addChild(pro1)
        root.addChild(pro2)
        root.addChild(pro3)
        return root
    }

}
