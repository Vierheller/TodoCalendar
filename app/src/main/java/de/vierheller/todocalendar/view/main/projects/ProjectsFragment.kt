package de.vierheller.todocalendar.view.main.projects


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import de.vierheller.todocalendar.R






/**
 * A simple [Fragment] subclass.
 */
class ProjectsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_projects, container, false) as FrameLayout

        val tView = AndroidTreeView(activity, createTree())
        view.addView(tView.view)


        return view
    }

    private fun createTree(): TreeNode {
        val root = TreeNode.root()
        val pro1 = TreeNode("Project1")
        val pro1_1 = TreeNode("Project1-1")
        val pro1_2 = TreeNode("Project1-2")
        pro1.addChild(pro1_1)
        pro1.addChild(pro1_2)

        val pro2 = TreeNode("Project2")

        val pro3 = TreeNode("Project3")
        val pro3_1 = TreeNode("Project3-1")
        val pro3_1_1 = TreeNode("Project3-1-1")
        pro3_1.addChild(pro3_1_1)
        pro3.addChild(pro3_1)


        root.addChild(pro1)
        root.addChild(pro2)
        root.addChild(pro3)
        return root
    }

}
