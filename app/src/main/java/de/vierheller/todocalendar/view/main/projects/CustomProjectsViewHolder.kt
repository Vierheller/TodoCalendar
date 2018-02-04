package de.vierheller.todocalendar.view.main.projects

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.unnamed.b.atv.model.TreeNode
import de.vierheller.todocalendar.R
import org.jetbrains.anko.find



/**
 * Created by Vierheller on 31.12.2017.
 */
class CustomProjectsViewHolder(context:Context) : TreeNode.BaseNodeViewHolder<ProjectItem>(context) {
    override fun createNodeView(node: TreeNode, value: ProjectItem): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_project, null, false)
        val titleName = view.find<TextView>(R.id.item_project_name)
        titleName.text = value.name
        setLayoutParams(titleName, value.level)
        return view
    }

    private fun setLayoutParams(nameView:TextView, level:Int){
        val marginLayoutParams = nameView.layoutParams as ViewGroup.MarginLayoutParams
        val dp30 = dpToPixel(this.context, 30f).toInt()
        marginLayoutParams.leftMargin = dp30 + dp30 * level
    }

    private fun dpToPixel(context:Context, dp:Float): Float {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }
}

data class ProjectItem(val database_id:Long, val name: String, val parent_id:Long, val level:Int)